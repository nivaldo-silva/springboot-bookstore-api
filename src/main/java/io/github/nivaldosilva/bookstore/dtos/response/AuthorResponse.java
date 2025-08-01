package io.github.nivaldosilva.bookstore.dtos.response;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
public record AuthorResponse(

    @Schema(description = "ID único do autor")
    UUID id,

    @Schema(description = "Nome do autor")
    String name,

    @Schema(description = "Nacionalidade do autor")
    String nationality,

    @Schema(description = "Data de nascimento do autor")
    LocalDate birthDate,

    @Schema(description = "Biografia do autor")
    String biography,

    @Schema(description = "Data de criação do registro")
    LocalDateTime createdAt,

    @Schema(description = "Data da última atualização do registro")
    LocalDateTime updatedAt
) {}
