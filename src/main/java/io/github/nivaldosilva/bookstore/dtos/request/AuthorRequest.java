package io.github.nivaldosilva.bookstore.dtos.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import java.time.LocalDate;
import java.util.UUID;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Dados para requisição de Autor (criação/atualização)")
public record AuthorRequest(

    @Schema(description = "ID único do autor (opcional para criação)")
    UUID id,

    @Schema(description = "Nome do autor")
    @NotBlank(message = "Name cannot be empty")
    @Size(max = 100, message = "Name cannot exceed 100 characters")
    String name,

    @Schema(description = "Nacionalidade do autor")
    @NotBlank(message = "Nationality cannot be empty")
    @Size(max = 100, message = "Nationality cannot exceed 100 characters")
    String nationality,

    @Schema(description = "Data de nascimento do autor")
    @NotNull(message = "Birth date cannot be null")
    @Past(message = "Birth date must be in the past")
    LocalDate birthDate,

    @Schema(description = "Biografia do autor")
    String biography
) {}
