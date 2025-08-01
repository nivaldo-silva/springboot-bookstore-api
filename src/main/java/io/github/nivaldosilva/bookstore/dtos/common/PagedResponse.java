package io.github.nivaldosilva.bookstore.dtos.common;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Resposta paginada genérica da API")
public record PagedResponse <T>(

    @Schema(description = "Conteúdo da página atual")
    List<T> content,

    @Schema(description = "Número da página atual (baseado em zero)")
    int pageNumber,

    @Schema(description = "Tamanho da página")
    int pageSize,

    @Schema(description = "Número total de elementos em todas as páginas")
    long totalElements,

    @Schema(description = "Número total de páginas")
    int totalPages,

    @Schema(description = "Indica se é a primeira página")
    boolean first,

    @Schema(description = "Indica se é a última página")
    boolean last
) {}
