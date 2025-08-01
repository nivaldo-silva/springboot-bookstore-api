package io.github.nivaldosilva.bookstore.dtos.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.github.nivaldosilva.bookstore.enums.Genre;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime; 
import java.util.UUID;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Dados de resposta do Livro")
public record BookResponse(
    @Schema(description = "ID único do livro")
    UUID id,

    @Schema(description = "ISBN do livro")
    String isbn,

    @Schema(description = "Título do livro")
    String title,

    @Schema(description = "Sinopse do livro")
    String synopsis,

    @Schema(description = "Gênero do livro")
    Genre genre,

    @Schema(description = "Data de publicação do livro")
    LocalDate publicationDate,

    @Schema(description = "Preço do livro")
    BigDecimal price,

    @Schema(description = "Quantidade em estoque do livro")
    Integer stockQuantity,

    @Schema(description = "ID do autor do livro")
    UUID authorId,

    @Schema(description = "Nome do autor do livro")
    String authorName,

    @Schema(description = "Data de criação do registro")
    LocalDateTime createdAt,

    @Schema(description = "Data da última atualização do registro")
    LocalDateTime updatedAt
) {}
