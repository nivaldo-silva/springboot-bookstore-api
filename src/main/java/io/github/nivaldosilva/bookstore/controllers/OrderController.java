package io.github.nivaldosilva.bookstore.controllers;

import io.github.nivaldosilva.bookstore.dtos.request.OrderRequest;
import io.github.nivaldosilva.bookstore.dtos.response.OrderResponse;
import io.github.nivaldosilva.bookstore.enums.OrderStatus;
import io.github.nivaldosilva.bookstore.services.interfaces.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = "/api/v1/orders", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Tag(name = "Pedidos", description = "Operações relacionadas ao gerenciamento de pedidos de livros")
public class OrderController {

    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);
    private final OrderService orderService;

    @Operation(summary = "Criar um novo pedido", description = "Cria um novo pedido no sistema.")
    @ApiResponse(responseCode = "201", description = "Pedido criado com sucesso.")
    @ApiResponse(responseCode = "400", description = "Dados inválidos ou estoque insuficiente.")
    @ApiResponse(responseCode = "404", description = "Cliente ou livro(s) não encontrado(s).")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OrderResponse> createOrder(
            @Parameter(description = "Dados do pedido a ser criado", required = true) @Valid @RequestBody OrderRequest orderRequest) {
        logger.info("Starting order creation for customer email: {}", orderRequest.customerEmail());
        OrderResponse createdOrder = orderService.createOrder(orderRequest);
        logger.info("Order created successfully.");
        return ResponseEntity.status(HttpStatus.CREATED).body(createdOrder);
    }

    @Operation(summary = "Buscar pedido por ID", description = "Recupera os detalhes de um pedido específico pelo seu ID.")
    @ApiResponse(responseCode = "200", description = "Pedido encontrado com sucesso.")
    @ApiResponse(responseCode = "404", description = "Pedido não encontrado.")
    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getOrderById(
            @Parameter(description = "ID único do pedido", required = true) @PathVariable UUID id) {
        logger.info("Starting order search by ID: {}", id);
        OrderResponse orderResponse = orderService.findOrderById(id);
        logger.info("Request found successfully.");
        return ResponseEntity.ok(orderResponse);
    }

    @Operation(summary = "Listar todos os pedidos", description = "Retorna uma lista de todos os pedidos registrados.")
    @ApiResponse(responseCode = "200", description = "Lista de pedidos recuperada com sucesso.")
    @GetMapping
    public ResponseEntity<List<OrderResponse>> getAllOrders() {
        logger.info("Starting search for all orders.");
        List<OrderResponse> orders = orderService.findAllOrders();
        logger.info("Search for all orders completed.");
        return ResponseEntity.ok(orders);
    }

    @Operation(summary = "Atualizar status do pedido", description = "Atualiza o status de um pedido existente.")
    @ApiResponse(responseCode = "200", description = "Status do pedido atualizado com sucesso.")
    @ApiResponse(responseCode = "400", description = "Status inválido ou dados fornecidos.")
    @ApiResponse(responseCode = "404", description = "Pedido não encontrado.")
    @PatchMapping("/{id}/status")
    public ResponseEntity<OrderResponse> updateOrderStatus(
            @Parameter(description = "ID único do pedido", required = true) @PathVariable UUID id,
            @Parameter(description = "Novo status do pedido", required = true, example = "SHIPPED") @RequestParam OrderStatus status) {
        logger.info("Starting order status update for order {} to: {}", id, status);
        OrderResponse updatedOrder = orderService.updateOrderStatus(id, status);
        logger.info("Order status updated successfully.");
        return ResponseEntity.ok(updatedOrder);
    }

    @Operation(summary = "Excluir pedido", description = "Remove um pedido do sistema.")
    @ApiResponse(responseCode = "204", description = "Pedido excluído com sucesso.")
    @ApiResponse(responseCode = "404", description = "Pedido não encontrado.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(
            @Parameter(description = "ID único do pedido", required = true) @PathVariable UUID id) {
        logger.info("Starting order deletion by ID: {}", id);
        orderService.deleteOrder(id);
        logger.info("Order deleted successfully.");
        return ResponseEntity.noContent().build();
    }
}
