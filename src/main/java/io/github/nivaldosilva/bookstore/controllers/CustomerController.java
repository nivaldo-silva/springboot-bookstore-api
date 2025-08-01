package io.github.nivaldosilva.bookstore.controllers;

import io.github.nivaldosilva.bookstore.dtos.request.CustomerRequest;
import io.github.nivaldosilva.bookstore.dtos.response.CustomerResponse;
import io.github.nivaldosilva.bookstore.services.interfaces.CustomerService;
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
@RequestMapping(value = "/api/v1/customers", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Tag(name = "Clientes", description = "Operações relacionadas ao gerenciamento de clientes")
public class CustomerController {

    private static final Logger logger = LoggerFactory.getLogger(CustomerController.class);
    private final CustomerService customerService;

    @Operation(summary = "Registrar um novo cliente", description = "Registra um novo cliente no sistema.")
    @ApiResponse(responseCode = "201", description = "Cliente registrado com sucesso.")
    @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos.")
    @ApiResponse(responseCode = "409", description = "Cliente com o e-mail já existe.")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CustomerResponse> registerCustomer(
            @Parameter(description = "Dados do cliente a ser registrado", required = true) @Valid @RequestBody CustomerRequest customerRequest) {
        logger.info("Starting customer registration for email: {}", customerRequest.email());
        CustomerResponse registeredCustomer = customerService.registerCustomer(customerRequest);
        logger.info("Customer registered successfully.");
        return ResponseEntity.status(HttpStatus.CREATED).body(registeredCustomer);
    }

    @Operation(summary = "Buscar cliente por ID", description = "Recupera os detalhes de um cliente específico pelo seu ID.")
    @ApiResponse(responseCode = "200", description = "Cliente encontrado com sucesso.")
    @ApiResponse(responseCode = "404", description = "Cliente não encontrado.")
    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponse> getCustomerById(
            @Parameter(description = "ID único do cliente", required = true) @PathVariable("id") UUID id) {
        logger.info("Starting customer search by ID: {}", id);
        CustomerResponse customerResponse = customerService.findCustomerById(id);
        logger.info("Customer found successfully.");
        return ResponseEntity.ok(customerResponse);
    }

    @Operation(summary = "Listar todos os clientes", description = "Retorna uma lista de todos os clientes registrados.")
    @ApiResponse(responseCode = "200", description = "Lista de clientes recuperada com sucesso.")
    @GetMapping
    public ResponseEntity<List<CustomerResponse>> getAllCustomers() {
        logger.info("Starting search for all customers.");
        List<CustomerResponse> customers = customerService.findAllCustomers();
        logger.info("Search for all customers completed.");
        return ResponseEntity.ok(customers);
    }

    @Operation(summary = "Atualizar cliente", description = "Atualiza os dados de um cliente existente. A senha pode ser atualizada se fornecida na requisição.")
    @ApiResponse(responseCode = "200", description = "Cliente atualizado com sucesso.")
    @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos.")
    @ApiResponse(responseCode = "404", description = "Cliente não encontrado.")
    @ApiResponse(responseCode = "409", description = "Cliente com o e-mail já existe.")
    @PutMapping("/{id}")
    public ResponseEntity<CustomerResponse> updateCustomer(
            @Parameter(description = "ID único do cliente", required = true) @PathVariable("id") UUID id,
            @Parameter(description = "Dados atualizados do cliente", required = true) @Valid @RequestBody CustomerRequest customerRequest) {
        logger.info("Starting customer update for ID: {}", id);
        CustomerResponse updatedCustomer = customerService.updateCustomer(id, customerRequest);
        logger.info("Customer updated successfully.");
        return ResponseEntity.ok(updatedCustomer);
    }

    @Operation(summary = "Excluir cliente", description = "Remove um cliente do sistema.")
    @ApiResponse(responseCode = "204", description = "Cliente excluído com sucesso.")
    @ApiResponse(responseCode = "404", description = "Cliente não encontrado.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(
            @Parameter(description = "ID único do cliente", required = true) @PathVariable("id") UUID id) {
        logger.info("Starting customer deletion by ID: {}", id);
        customerService.deleteCustomer(id);
        logger.info("Customer successfully deleted.");
        return ResponseEntity.noContent().build();
    }

}