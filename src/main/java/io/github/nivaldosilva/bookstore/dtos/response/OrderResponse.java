package io.github.nivaldosilva.bookstore.dtos.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.github.nivaldosilva.bookstore.enums.OrderStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Dados de resposta do Pedido")
public record OrderResponse(
    
    @Schema(description = "ID único do pedido")
    UUID id,

    @Schema(description = "Email do cliente que fez o pedido")
    String customerEmail,

    @Schema(description = "Nome completo do cliente que fez o pedido")
    String customerFullName,

    @Schema(description = "Lista de itens do pedido")
    List<OrderItemResponse> items, 

    @Schema(description = "Valor total do pedido")
    BigDecimal totalAmount,

    @Schema(description = "Status atual do pedido")
    OrderStatus status,

    @Schema(description = "Data e hora de criação do pedido")
    LocalDateTime createdAt,

    @Schema(description = "Data e hora da última atualização do pedido")
    LocalDateTime updatedAt
) {}
