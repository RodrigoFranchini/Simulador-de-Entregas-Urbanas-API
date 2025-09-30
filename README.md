# Sistema de simulação de entregas por drone

## Sobre o projeto:

O Simulador de Entregas é uma API desenvolvida em Java Spring Boot que simula entregas urbanas utilizando drones. O sistema gerencia pedidos (localização do cliente, peso e prioridade) e aloca drones de forma otimizada, respeitando restrições de capacidade e distância.
O objetivo é alocar pedidos em drones com o menor número de viagens possível, respeitando:

- Capacidade (kg) e alcance (km) de cada drone.

- Mapa em malha (coordenadas 1A..4D).

- Prioridade de entrega: ALTA > MÉDIA > BAIXA.

---
## Arquitetura

- Java 17 + Spring Boot 3 + Maven 
- Banco em memória H2
- Camadas: 
1. Entities → entidades JPA (Drone, Pedido, Viagem, Entrega, Cliente)
   - Dto → objetos de resposta da API
1. Repositories → persistência com Spring Data JPA
1. Services → regras de negócio (alocação, fila, finalização de viagem)
    - Impl (implementação da lógica do services)
1. Controllers/resources → endpoints REST

---

## Como rodar o projeto

### Pré requisitos: 
-  Java  (instalado e no path)
- Maven 3.8 (instalado e no path)

1. Clonar o repositório
```bash 
git clone <URL-do-repositório>
cd urban-delivery-simulator
````

2. Rodar o projeto
```bash
./mvnw spring-boot:run
```
---
## Endpoints

- Aplicação principal: http://localhost:8080

- Console do H2: http://localhost:8080/h2-console

- GET /pedidos -> retorna a lista com todos os pedidos

- POST /pedidos -> adiciona um novo pedido, exemplo de formato abaixo
```bash
{
  "destinoGrid": "2A",
  "pesoKg": 1.0,
  "prioridade": "ALTA"
} 
```

- POST /alocacoes/run -> aloca os pedidos a viagens, respeitando o limite da capacidadeKG dos drones
- GET /fila/viagens -> retorna uma lista de todas viagens
- POST /viagens/{id}/finalizar -> finaiza uma viagem, indica o status como CONCLUIDA
- GET /drones/status -> retorna o status de todos os drones registrados
- GET /drones/{id}/status -> retorna o status de um drone especificado pelo id
- GET /drones/status?status=DISPONIVEL -> retorna drones que tem um status específico

---
## Créditos

- Projeto desenvolvido por [Rodrigo Franchini Cecchin](https://portifolio-profissional-virid.vercel.app/)



