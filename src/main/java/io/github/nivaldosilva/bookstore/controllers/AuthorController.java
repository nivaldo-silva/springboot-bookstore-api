package io.github.nivaldosilva.bookstore.controllers;

import io.github.nivaldosilva.bookstore.dtos.request.AuthorRequest;
import io.github.nivaldosilva.bookstore.dtos.response.AuthorResponse;
import io.github.nivaldosilva.bookstore.services.interfaces.AuthorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/authors", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Autores", description = "Operações relacionadas ao gerenciamento de autores de livros")
public class AuthorController {

    private static final Logger logger = LoggerFactory.getLogger(AuthorController.class);
    private final AuthorService authorService;

    @Operation(summary = "Criar um novo autor", description = "Cria um novo autor no sistema com os dados fornecidos.")
    @ApiResponse(responseCode = "201", description = "Autor criado com sucesso.")
    @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos.")
    @ApiResponse(responseCode = "409", description = "Autor com o nome já existe.")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AuthorResponse> createAuthor(@Parameter(description = "Dados do autor a ser criado", required = true) @Valid @RequestBody AuthorRequest authorRequest) {
        logger.info("Starting author creation for name: {}", authorRequest.name());
        AuthorResponse createdAuthor = authorService.createAuthor(authorRequest);
        logger.info("Author created successfully.");
        return ResponseEntity.status(HttpStatus.CREATED).body(createdAuthor);
    }

    @Operation(summary = "Buscar autor por ID", description = "Recupera os detalhes de um autor específico pelo seu ID.")
    @ApiResponse(responseCode = "200", description = "Autor encontrado com sucesso.")
    @ApiResponse(responseCode = "404", description = "Autor não encontrado.")
    @GetMapping("/{id}")
    public ResponseEntity<AuthorResponse> getAuthorById(
            @Parameter(description = "ID único do autor", required = true) @PathVariable UUID id) {
        logger.info("Starting author search by ID: {}", id);
        AuthorResponse authorResponse = authorService.findAuthorById(id);
        logger.info("Author found successfully.");
        return ResponseEntity.ok(authorResponse);
    }

    @Operation(summary = "Listar todos os autores", description = "Retorna uma lista de todos os autores registrados.")
    @ApiResponse(responseCode = "200", description = "Lista de autores recuperada com sucesso.")
    @GetMapping
    public ResponseEntity<List<AuthorResponse>> getAllAuthors() {
        logger.info("Starting search for all authors.");
        List<AuthorResponse> authors = authorService.findAllAuthors();
        logger.info("Search for all authors completed.");
        return ResponseEntity.ok(authors);
    }

    @Operation(summary = "Atualizar autor", description = "Atualiza os dados de um autor existente.")
    @ApiResponse(responseCode = "200", description = "Autor atualizado com sucesso.")
    @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos.")
    @ApiResponse(responseCode = "404", description = "Autor não encontrado.")
    @ApiResponse(responseCode = "409", description = "Autor com o nome já existe.")
    @PutMapping("/{id}")
    public ResponseEntity<AuthorResponse> updateAuthor(
            @Parameter(description = "ID único do autor", required = true) @PathVariable UUID id,
            @Parameter(description = "Dados atualizados do autor", required = true) @Valid @RequestBody AuthorRequest authorRequest) {
        logger.info("Starting author update for ID: {}", id);
        AuthorResponse updatedAuthor = authorService.updateAuthor(id, authorRequest);
        logger.info("Author updated successfully.");
        return ResponseEntity.ok(updatedAuthor);
    }

    @Operation(summary = "Excluir autor", description = "Remove um autor do sistema.")
    @ApiResponse(responseCode = "204", description = "Autor excluído com sucesso.")
    @ApiResponse(responseCode = "404", description = "Autor não encontrado.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAuthor(
            @Parameter(description = "ID único do autor", required = true) @PathVariable UUID id) {
        logger.info("Starting author deletion by ID: {}", id);
        authorService.deleteAuthor(id);
        logger.info("Author deleted successfully.");
        return ResponseEntity.noContent().build();
    }
}