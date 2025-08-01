package io.github.nivaldosilva.bookstore.controllers;

import io.github.nivaldosilva.bookstore.dtos.common.PagedResponse;
import io.github.nivaldosilva.bookstore.dtos.request.BookRequest;
import io.github.nivaldosilva.bookstore.dtos.response.BookResponse;
import io.github.nivaldosilva.bookstore.enums.Genre;
import io.github.nivaldosilva.bookstore.services.interfaces.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/books", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Livros", description = "Operações de gerenciamento de livros")
public class BookController {

    private static final Logger logger = LoggerFactory.getLogger(BookController.class);
    private final BookService bookService;

    @Operation(summary = "Criar um novo livro", description = "Cria um novo livro no sistema com os dados fornecidos.")
    @ApiResponse(responseCode = "201", description = "Livro criado com sucesso.")
    @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos.")
    @ApiResponse(responseCode = "404", description = "Autor não encontrado.")
    @ApiResponse(responseCode = "409", description = "Livro com ISBN já existe.")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BookResponse> createBook(
            @Parameter(description = "Dados do livro a ser criado", required = true) @Valid @RequestBody BookRequest bookRequest) {
        logger.info("Starting book creation with ISBN: {}", bookRequest.isbn());
        BookResponse createdBook = bookService.createBook(bookRequest);
        logger.info("Book created successfully.");
        return ResponseEntity.status(HttpStatus.CREATED).body(createdBook);
    }

    @Operation(summary = "Buscar livro por ID", description = "Recupera os detalhes de um livro específico pelo seu ID.")
    @ApiResponse(responseCode = "200", description = "Livro encontrado com sucesso.")
    @ApiResponse(responseCode = "404", description = "Livro não encontrado.")
    @GetMapping("/{id}")
    public ResponseEntity<BookResponse> getBookById(
            @Parameter(description = "ID único do livro", required = true) @PathVariable UUID id) {
        logger.info("Starting book search by ID: {}", id);
        BookResponse bookResponse = bookService.findBookById(id);
        logger.info("Book found successfully.");
        return ResponseEntity.ok(bookResponse);
    }

    @Operation(summary = "Listar todos os livros", description = "Retorna uma lista paginada de livros com opções de filtro por gênero e autor.")
    @ApiResponse(responseCode = "200", description = "Lista de livros recuperada com sucesso.")
    @GetMapping
    public ResponseEntity<PagedResponse<BookResponse>> findAllBooks(
            @Parameter(description = "Número da página (0-indexed)", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Tamanho da página", example = "10") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Campo para ordenação", example = "title") @RequestParam(defaultValue = "title") String sortBy,
            @Parameter(description = "Direção da ordenação (ASC/DESC)", example = "ASC") @RequestParam(defaultValue = "ASC") String sortDirection,
            @Parameter(description = "Gênero para filtrar", example = "FANTASY") @RequestParam(required = false) Genre genre,
            @Parameter(description = "ID do autor para filtrar") @RequestParam(required = false) UUID authorId) {

        logger.info(
                "Starting book search with pagination: page={}, size={}, sortBy={}, sortDirection={}, genre={}, authorId={}",
                page, size, sortBy, sortDirection, genre, authorId);

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.fromString(sortDirection), sortBy);

        PagedResponse<BookResponse> response = bookService.findAllBooks(pageable, genre, authorId);

        logger.info("Book search completed. Total elements: {}", response.totalElements());
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Atualizar livro", description = "Atualiza os dados de um livro existente.")
    @ApiResponse(responseCode = "200", description = "Livro atualizado com sucesso.")
    @ApiResponse(responseCode = "400", description = "Dados inválidos.")
    @ApiResponse(responseCode = "404", description = "Livro ou autor não encontrado.")
    @ApiResponse(responseCode = "409", description = "Livro com ISBN já existe.")
    @PutMapping("/{id}")
    public ResponseEntity<BookResponse> updateBook(
            @Parameter(description = "ID único do livro", required = true) @PathVariable UUID id,
            @Parameter(description = "Dados atualizados do livro", required = true) @Valid @RequestBody BookRequest bookRequest) {
        logger.info("Starting book update for ID: {}", id);
        BookResponse updatedBook = bookService.updateBook(id, bookRequest);
        logger.info("Book updated successfully.");
        return ResponseEntity.ok(updatedBook);
    }

    @Operation(summary = "Excluir livro", description = "Remove um livro do sistema.")
    @ApiResponse(responseCode = "204", description = "Livro excluído com sucesso.")
    @ApiResponse(responseCode = "404", description = "Livro não encontrado.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(
            @Parameter(description = "ID único do livro", required = true) @PathVariable UUID id) {
        logger.info("Starting book deletion by ID: {}", id);
        bookService.deleteBook(id);
        logger.info("Book deleted successfully.");
        return ResponseEntity.noContent().build();
    }
}