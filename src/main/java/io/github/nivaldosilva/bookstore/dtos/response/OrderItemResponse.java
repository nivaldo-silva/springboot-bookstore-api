package io.github.nivaldosilva.bookstore.dtos.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import java.math.BigDecimal;
import java.util.UUID;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Dados de resposta do Item de Pedido")
public record OrderItemResponse(

    @Schema(description = "ID único do item de pedido")
    UUID id,

    @Schema(description = "ID único do livro")
    UUID bookId,

    @Schema(description = "ISBN do livro")
    String bookIsbn,

    @Schema(description = "Título do livro")
    String bookTitle,

    @Schema(description = "Nome do autor do livro")
    String bookAuthorName,

    @Schema(description = "Quantidade do livro")
    Integer quantity,

    @Schema(description = "Preço unitário do livro")
    BigDecimal unitPrice,

    @Schema(description = "Preço total do livro")
    BigDecimal totalPrice
) {}

