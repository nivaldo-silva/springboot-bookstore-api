package io.github.nivaldosilva.bookstore.dtos.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import java.time.LocalDateTime; 
import java.util.List;
import java.util.UUID;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Dados de resposta do Cliente")
public record CustomerResponse(
    
    @Schema(description = "ID único do cliente")
    UUID id,

    @Schema(description = "Nome completo do cliente")
    String fullName,

    @Schema(description = "Email do cliente")
    String email,

    @Schema(description = "Lista de pedidos do cliente")
    List<OrderResponse> orders, 

    @Schema(description = "Data de criação do registro")
    LocalDateTime createdAt,

    @Schema(description = "Data da última atualização do registro")
    LocalDateTime updatedAt
) {}
