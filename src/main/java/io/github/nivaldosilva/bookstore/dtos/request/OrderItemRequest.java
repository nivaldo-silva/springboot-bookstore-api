package io.github.nivaldosilva.bookstore.dtos.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import org.hibernate.validator.constraints.ISBN;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Dados para requisição de Item de Pedido")
public record OrderItemRequest(

    @Schema(description = "ISBN do livro")
    @NotBlank(message = "Book ISBN cannot be empty")
    @ISBN(type = ISBN.Type.ANY, message = "Invalid ISBN format")
    String bookIsbn,

    @Schema(description = "Quantidade do livro")
    @NotNull(message = "Quantity cannot be null")
    @Positive(message = "Quantity must be positive")
    Integer quantity
) {}
