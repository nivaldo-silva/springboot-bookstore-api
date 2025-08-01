package io.github.nivaldosilva.bookstore.dtos.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;
import java.util.UUID;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Dados para requisição de Pedido (criação)")
public record OrderRequest(

    @Schema(description = "ID único do pedido (opcional para criação)")
    UUID id,

    @Schema(description = "Email do cliente que está fazendo o pedido")
    @NotBlank(message = "Customer email cannot be empty")
    @Email(message = "Invalid customer email format")
    String customerEmail,

    @Schema(description = "Lista de itens do pedido")
    @NotNull(message = "Order items cannot be null")
    @NotEmpty(message = "Order must contain at least one item")
    List<OrderItemRequest> items 
) {}