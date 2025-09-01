package com.deliverysimulator.urbandelivery.services.impl;

import com.deliverysimulator.urbandelivery.entities.*;
import com.deliverysimulator.urbandelivery.repositories.*;
import com.deliverysimulator.urbandelivery.services.AlocacaoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class AlocacaoServiceImpl implements AlocacaoService {

    private final DroneRepository droneRepository;
    private final PedidoRepository pedidoRepository;
    private final ViagemRepository viagemRepository;
    private final EntregaRepository entregaRepository;

    public AlocacaoServiceImpl(DroneRepository droneRepository,
                               PedidoRepository pedidoRepository,
                               ViagemRepository viagemRepository,
                               EntregaRepository entregaRepository) {
        this.droneRepository = droneRepository;
        this.pedidoRepository = pedidoRepository;
        this.viagemRepository = viagemRepository;
        this.entregaRepository = entregaRepository;
    }

    @Override
    @Transactional
    public int alocarPedidos() {
        // 1) Drones disponíveis
        List<Drone> dronesDisponiveis = droneRepository.findAll().stream()
                .filter(d -> d.getStatus() == StatusDrone.DISPONIVEL)
                .collect(Collectors.toList());
        if (dronesDisponiveis.isEmpty()) return 0;

        // 2) Pedidos em viagens "em preparação" (viagens sem drone)
        List<Viagem> viagensSemDrone = viagemRepository.findAll().stream()
                .filter(v -> v.getDrone() == null && v.getStatus() == StatusViagem.PLANEJADA)
                .collect(Collectors.toList());
        if (viagensSemDrone.isEmpty()) return 0;

        // Extrai pedidos dessas viagens
        List<Pedido> pedidosPendentes = new ArrayList<>();
        Map<Integer, List<Entrega>> entregasPorViagem = new HashMap<>();

        for (Viagem v : viagensSemDrone) {
            List<Entrega> ents = entregaRepository.findByViagem_Id(v.getId());
            entregasPorViagem.put(v.getId(), ents);
            for (Entrega e : ents) {
                Pedido p = e.getPedido();
                if (p.getStatus() == StatusPedido.PENDENTE) pedidosPendentes.add(p);
            }
        }
        if (pedidosPendentes.isEmpty()) return 0;

        // 3) Ordena pedidos por prioridade (ALTA>MEDIA>BAIXA) e peso desc
        pedidosPendentes.sort((a, b) -> {
            int pa = prioridadeRank(a.getPrioridade());
            int pb = prioridadeRank(b.getPrioridade());
            if (pa != pb) return Integer.compare(pb, pa);
            return Double.compare(b.getPesoKg(), a.getPesoKg());
        });

        int viagensCriadas = 0;

        // 4) Bin-packing por drone: monta novas viagens (com drone)
        for (Drone drone : dronesDisponiveis) {
            double capacidade = drone.getCapacidadeKg();
            double alcance = drone.getAlcanceKm();

            List<List<Pedido>> bins = new ArrayList<>();

            for (Pedido pedido : pedidosPendentes) {
                if (pedido.getStatus() != StatusPedido.PENDENTE) continue;

                // ignora grid inválido
                if (!gridValido(pedido.getDestinoGrid())) continue;

                if (pedido.getPesoKg() > capacidade) continue; // inviável nesse drone

                boolean alocado = false;
                for (List<Pedido> bin : bins) {
                    double pesoAtual = bin.stream().mapToDouble(Pedido::getPesoKg).sum();
                    if (pesoAtual + pedido.getPesoKg() <= capacidade) {
                        double dist = distanciaAproxIdaVolta(bin, pedido);
                        if (dist <= alcance) {
                            bin.add(pedido);
                            alocado = true;
                            break;
                        }
                    }
                }
                if (!alocado) {
                    if (idaVolta(pedido.getDestinoGrid()) <= alcance) {
                        List<Pedido> novo = new ArrayList<>();
                        novo.add(pedido);
                        bins.add(novo);
                    }
                }
            }

            // 5) Persistência: cria Viagem com drone e move as entregas
            for (List<Pedido> grupo : bins) {
                if (grupo.isEmpty()) continue;

                double pesoTotal = grupo.stream().mapToDouble(Pedido::getPesoKg).sum();
                double distanciaTotal = distanciaAproxIdaVolta(grupo);

                Viagem nova = new Viagem(drone, distanciaTotal, pesoTotal);
                nova.setStatus(StatusViagem.PLANEJADA);
                nova = viagemRepository.save(nova);

                for (Pedido p : grupo) {
                    // encontra a Entrega antiga (viagem sem drone) e MOVE para a nova viagem
                    Entrega ent = entregaRepository.findAll().stream()
                            .filter(e -> e.getPedido().getId() == p.getId())
                            .findFirst()
                            .orElse(null);
                    if (ent != null) {
                        ent.setViagem(nova);
                        ent.setStatus(StatusEntrega.PENDENTE);
                        entregaRepository.save(ent);
                    }
                    p.setStatus(StatusPedido.ALOCADO);
                    pedidoRepository.save(p);
                }

                // marca drone reservado
                drone.setStatus(StatusDrone.EM_VIAGEM);
                droneRepository.save(drone);

                viagensCriadas++;
            }
        }

        // 6) Limpeza: remove viagens antigas que ficaram sem entregas
        for (Viagem v : viagensSemDrone) {
            boolean aindaTem = !entregaRepository.findByViagem_Id(v.getId()).isEmpty();
            if (!aindaTem) viagemRepository.delete(v);
        }

        return viagensCriadas;
    }

    // Helpers
    private int prioridadeRank(PrioridadeEntrega p) {
        if (p == PrioridadeEntrega.ALTA) return 3;
        if (p == PrioridadeEntrega.MEDIA) return 2;
        return 1;
    }

    private boolean gridValido(String g) {
        return g != null && g.length() == 2 &&
                g.charAt(0) >= '1' && g.charAt(0) <= '4' &&
                Character.toUpperCase(g.charAt(1)) >= 'A' &&
                Character.toUpperCase(g.charAt(1)) <= 'D';
    }

    private double distBase(String g) {
        int x = g.charAt(0) - '0';
        int y = Character.toUpperCase(g.charAt(1)) - 'A' + 1;
        return Math.hypot(x, y);
    }

    private double idaVolta(String grid) {
        return 2.0 * distBase(grid);
    }

    private double distanciaAproxIdaVolta(List<Pedido> grupo) {
        double max = 0.0;
        for (Pedido p : grupo) max = Math.max(max, distBase(p.getDestinoGrid()));
        return 2.0 * max;
    }

    private double distanciaAproxIdaVolta(List<Pedido> grupo, Pedido cand) {
        double max = distBase(cand.getDestinoGrid());
        for (Pedido p : grupo) max = Math.max(max, distBase(p.getDestinoGrid()));
        return 2.0 * max;
    }
}
