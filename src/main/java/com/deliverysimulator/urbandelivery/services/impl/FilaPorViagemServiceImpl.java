package com.deliverysimulator.urbandelivery.services.impl;

import com.deliverysimulator.urbandelivery.entities.dto.FilaEntregaDTO;
import com.deliverysimulator.urbandelivery.entities.dto.FilaViagemDTO;
import com.deliverysimulator.urbandelivery.entities.Entrega;
import com.deliverysimulator.urbandelivery.entities.Pedido;
import com.deliverysimulator.urbandelivery.entities.PrioridadeEntrega;
import com.deliverysimulator.urbandelivery.entities.StatusViagem;
import com.deliverysimulator.urbandelivery.entities.Viagem;
import com.deliverysimulator.urbandelivery.repositories.EntregaRepository;
import com.deliverysimulator.urbandelivery.repositories.ViagemRepository;
import com.deliverysimulator.urbandelivery.services.FilaPorViagemService;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class FilaPorViagemServiceImpl implements FilaPorViagemService {

    private final EntregaRepository entregaRepository;
    private final ViagemRepository viagemRepository;

    public FilaPorViagemServiceImpl(EntregaRepository entregaRepository,
                                    ViagemRepository viagemRepository) {
        this.entregaRepository = entregaRepository;
        this.viagemRepository = viagemRepository;
    }

    @Override
    public List<FilaViagemDTO> gerarFilaPorViagem() {
        // 1) Carrega viagens e mantém apenas as que já têm drone
        List<Viagem> viagens = viagemRepository.findAll().stream()
                .filter(v -> v.getDrone() != null)
                .collect(Collectors.toList());

        // Ordena viagens por status e id
        viagens.sort(Comparator
                .comparingInt((Viagem v) -> statusRank(v.getStatus()))
                .thenComparingInt(Viagem::getId));

        // 2) Carrega todas as entregas e agrupa por viagem
        List<Entrega> entregas = entregaRepository.findAll();
        Map<Integer, List<Entrega>> entregasPorViagem = entregas.stream()
                .filter(e -> e.getViagem() != null && e.getViagem().getDrone() != null) // só viagens válidas
                .collect(Collectors.groupingBy(e -> e.getViagem().getId()));

        List<FilaViagemDTO> resultado = new ArrayList<>();

        // 3) Monta a fila por viagem
        for (Viagem v : viagens) {
            List<Entrega> lista = entregasPorViagem.getOrDefault(v.getId(), Collections.emptyList());
            if (lista.isEmpty()) continue;

            // Ordena dentro da viagem: prioridade (desc) > distância (asc)
            lista.sort((e1, e2) -> {
                int p1 = prioridadeRank(e1.getPedido().getPrioridade());
                int p2 = prioridadeRank(e2.getPedido().getPrioridade());
                if (p1 != p2) return Integer.compare(p2, p1); // ALTA > MEDIA > BAIXA
                double d1 = distBase(e1.getPedido().getDestinoGrid());
                double d2 = distBase(e2.getPedido().getDestinoGrid());
                return Double.compare(d1, d2); // mais perto primeiro
            });

            int ordem = 1;
            List<FilaEntregaDTO> itens = new ArrayList<>();
            for (Entrega ent : lista) {
                Pedido p = ent.getPedido();
                itens.add(new FilaEntregaDTO(
                        p.getId(),
                        p.getDestinoGrid(),
                        distBase(p.getDestinoGrid()),
                        p.getPesoKg(),
                        p.getPrioridade(),
                        p.getStatus(),
                        ordem++
                ));
            }

            resultado.add(new FilaViagemDTO(
                    v.getId(),
                    v.getDrone() != null ? v.getDrone().getId() : null,
                    v.getStatus(),
                    itens
            ));
        }

        return resultado;
    }

    // -------------------
    // Helpers
    // -------------------
    private int prioridadeRank(PrioridadeEntrega p) {
        if (p == PrioridadeEntrega.ALTA) return 3;
        if (p == PrioridadeEntrega.MEDIA) return 2;
        return 1;
    }

    private int statusRank(StatusViagem s) {
        if (s == StatusViagem.EM_ANDAMENTO) return 1;
        if (s == StatusViagem.PLANEJADA) return 2;
        if (s == StatusViagem.CONCLUIDA) return 3;
        return 4; // CANCELADA ou outros
    }

    // Distância da base (0,0) mapeando '1A'..'4D' -> (x=1..4, y=1..4)
    private double distBase(String g) {
        if (g == null || g.length() != 2) return Double.MAX_VALUE;
        int x = g.charAt(0) - '0';
        int y = Character.toUpperCase(g.charAt(1)) - 'A' + 1;
        return Math.hypot(x, y);
    }
}
