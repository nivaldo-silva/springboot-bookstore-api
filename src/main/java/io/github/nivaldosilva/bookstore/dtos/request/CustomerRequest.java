package io.github.nivaldosilva.bookstore.dtos.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import java.util.UUID;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Dados para requisição de Cliente (criação/atualização)")
public record CustomerRequest(

    @Schema(description = "ID único do cliente (opcional para criação)")
    UUID id,

    @Schema(description = "Nome completo do cliente")
    @NotBlank(message = "Full name cannot be empty")
    @Size(max = 150, message = "Full name cannot exceed 150 characters")
    String fullName,

    @Schema(description = "Email do cliente")
    @NotBlank(message = "Email cannot be empty")
    @Email(message = "Invalid email format")
    @Size(max = 100, message = "Email cannot exceed 100 characters")
    String email,

    @Schema(description = "Senha do cliente (obrigatória na criação, opcional na atualização)")
    String password
) {} 