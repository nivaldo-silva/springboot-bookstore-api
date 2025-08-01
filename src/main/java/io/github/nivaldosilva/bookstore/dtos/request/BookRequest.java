package io.github.nivaldosilva.bookstore.dtos.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.github.nivaldosilva.bookstore.enums.Genre;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import org.hibernate.validator.constraints.ISBN;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Dados para requisição de Livro (criação/atualização)")
public record BookRequest(

  @Schema(description = "ID único do livro (opcional para criação)")
    UUID id,

    @Schema(description = "ISBN do livro")
    @NotBlank(message = "ISBN cannot be empty")
    @ISBN(type = ISBN.Type.ANY, message = "Invalid ISBN format")
    @Size(max = 17, message = "ISBN cannot exceed 17 characters")
    String isbn,

    @Schema(description = "Título do livro")
    @NotBlank(message = "Title cannot be empty")
    @Size(max = 200, message = "Title cannot exceed 200 characters")
    String title,

    @Schema(description = "Sinopse do livro")
    @NotBlank(message = "Synopsis cannot be empty")
    String synopsis,

    @Schema(description = "Gênero do livro")
    @NotNull(message = "Genre cannot be null")
    Genre genre,

    @Schema(description = "Data de publicação do livro")
    @NotNull(message = "Publication date cannot be null")
    @Past(message = "Publication date must be in the past")
    LocalDate publicationDate,

    @Schema(description = "Preço do livro")
    @NotNull(message = "Price cannot be null")
    @DecimalMin(value = "0.00", inclusive = false, message = "Price must be greater than zero")
    BigDecimal price,

    @Schema(description = "Quantidade em estoque do livro")
    @NotNull(message = "Stock quantity cannot be null")
    @PositiveOrZero(message = "Stock quantity cannot be negative")
    Integer stockQuantity,

    @Schema(description = "ID do autor do livro")
    @NotNull(message = "Author ID cannot be null")
    UUID authorId
) {}
