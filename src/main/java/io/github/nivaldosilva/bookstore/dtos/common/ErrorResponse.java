package io.github.nivaldosilva.bookstore.dtos.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Objeto de erro")
public record ErrorResponse(
       
        @Schema(description = "Mensagem de erro") 
        String message) {}