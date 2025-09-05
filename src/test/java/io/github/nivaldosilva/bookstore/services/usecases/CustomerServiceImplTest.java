package io.github.nivaldosilva.bookstore.services.usecases;

import io.github.nivaldosilva.bookstore.dtos.request.CustomerRequest;
import io.github.nivaldosilva.bookstore.dtos.response.CustomerResponse;
import io.github.nivaldosilva.bookstore.entities.Customer;
import io.github.nivaldosilva.bookstore.exceptions.CustomerNotFoundException;
import io.github.nivaldosilva.bookstore.exceptions.EmailAlreadyExistsException;
import io.github.nivaldosilva.bookstore.repositories.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("CustomerService Tests")
class CustomerServiceImplTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private CustomerServiceImpl customerService;

    private UUID customerId;
    private CustomerRequest customerRequest;
    private Customer customer;
    private String rawPassword;
    private String encryptedPassword;

    @BeforeEach
    void setUp() {
        customerId = UUID.randomUUID();
        rawPassword = "password123";
        encryptedPassword = "encryptedPassword";
        customerRequest = CustomerRequest.builder()
                .fullName("John Doe")
                .email("john.doe@example.com")
                .password(rawPassword)
                .build();
        customer = Customer.builder()
                .id(customerId)
                .fullName("John Doe")
                .email("john.doe@example.com")
                .password(encryptedPassword)
                .build();
    }

    @Test
    @DisplayName("Should register customer successfully")
    void shouldRegisterCustomerSuccessfully() {
        when(customerRepository.existsByEmail(customerRequest.email())).thenReturn(false);
        when(passwordEncoder.encode(rawPassword)).thenReturn(encryptedPassword);
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);
        CustomerResponse result = customerService.registerCustomer(customerRequest);
        assertNotNull(result);
        assertEquals(customer.getEmail(), result.email());
        verify(customerRepository).existsByEmail(customerRequest.email());
        verify(passwordEncoder).encode(rawPassword);
        verify(customerRepository).save(any(Customer.class));
    }

    @Test
    @DisplayName("Should throw EmailAlreadyExistsException when email already exists")
    void shouldThrowEmailAlreadyExistsException() {
        when(customerRepository.existsByEmail(customerRequest.email())).thenReturn(true);
        assertThrows(EmailAlreadyExistsException.class, () -> customerService.registerCustomer(customerRequest));
        verify(customerRepository).existsByEmail(customerRequest.email());
        verify(customerRepository, never()).save(any(Customer.class));
    }

    @Test
    @DisplayName("Should find customer by ID successfully")
    void shouldFindCustomerByIdSuccessfully() {
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        CustomerResponse result = customerService.findCustomerById(customerId);
        assertNotNull(result);
        assertEquals(customer.getId(), result.id());
        assertEquals(customer.getEmail(), result.email());
        verify(customerRepository).findById(customerId);
    }

    @Test
    @DisplayName("Should throw CustomerNotFoundException when customer not found by ID")
    void shouldThrowCustomerNotFoundExceptionWhenNotFound() {
        when(customerRepository.findById(customerId)).thenReturn(Optional.empty());
        assertThrows(CustomerNotFoundException.class, () -> customerService.findCustomerById(customerId));
        verify(customerRepository).findById(customerId);
    }

    @Test
    @DisplayName("Should find all customers successfully")
    void shouldFindAllCustomersSuccessfully() {
        List<Customer> customers = List.of(customer);
        when(customerRepository.findAll()).thenReturn(customers);
        List<CustomerResponse> result = customerService.findAllCustomers();
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(customer.getEmail(), result.get(0).email());
        verify(customerRepository).findAll();
    }

    @Test
    @DisplayName("Should update customer successfully with different email")
    void shouldUpdateCustomerSuccessfullyWithDifferentEmail() {
        CustomerRequest updateRequest = CustomerRequest.builder()
                .fullName("Jane Doe")
                .email("jane.doe@example.com")
                .password(rawPassword)
                .build();
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        when(customerRepository.existsByEmail(updateRequest.email())).thenReturn(false);
        when(passwordEncoder.encode(updateRequest.password())).thenReturn(encryptedPassword);
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);
        CustomerResponse result = customerService.updateCustomer(customerId, updateRequest);
        assertNotNull(result);
        assertEquals(updateRequest.email(), result.email());
        verify(customerRepository).findById(customerId);
        verify(customerRepository).existsByEmail(updateRequest.email());
        verify(passwordEncoder).encode(updateRequest.password());
        verify(customerRepository).save(any(Customer.class));
    }

    @Test
    @DisplayName("Should update customer with same email successfully")
    void shouldUpdateCustomerWithSameEmailSuccessfully() {
        CustomerRequest updateRequest = CustomerRequest.builder()
                .fullName("Jane Doe")
                .email(customer.getEmail())
                .password(rawPassword)
                .build();
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        when(passwordEncoder.encode(updateRequest.password())).thenReturn(encryptedPassword);
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);
        CustomerResponse result = customerService.updateCustomer(customerId, updateRequest);
        assertNotNull(result);
        assertEquals(updateRequest.email(), result.email());
        verify(customerRepository).findById(customerId);
        verify(customerRepository, never()).existsByEmail(anyString());
        verify(passwordEncoder).encode(updateRequest.password());
        verify(customerRepository).save(any(Customer.class));
    }

    @Test
    @DisplayName("Should throw CustomerNotFoundException when updating non-existing customer")
    void shouldThrowCustomerNotFoundExceptionWhenUpdating() {
        when(customerRepository.findById(customerId)).thenReturn(Optional.empty());
        assertThrows(CustomerNotFoundException.class,
                () -> customerService.updateCustomer(customerId, customerRequest));
        verify(customerRepository).findById(customerId);
        verify(customerRepository, never()).save(any(Customer.class));
    }

    @Test
    @DisplayName("Should throw EmailAlreadyExistsException when updating with existing email")
    void shouldThrowEmailAlreadyExistsExceptionWhenUpdating() {
        CustomerRequest updateRequest = CustomerRequest.builder()
                .fullName("Another User")
                .email("another.user@example.com")
                .password(rawPassword)
                .build();
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        when(customerRepository.existsByEmail(updateRequest.email())).thenReturn(true);
        assertThrows(EmailAlreadyExistsException.class,
                () -> customerService.updateCustomer(customerId, updateRequest));
        verify(customerRepository).findById(customerId);
        verify(customerRepository).existsByEmail(updateRequest.email());
        verify(customerRepository, never()).save(any(Customer.class));
    }

    @Test
    @DisplayName("Should delete customer successfully")
    void shouldDeleteCustomerSuccessfully() {
        when(customerRepository.existsById(customerId)).thenReturn(true);
        customerService.deleteCustomer(customerId);
        verify(customerRepository).existsById(customerId);
        verify(customerRepository).deleteById(customerId);
    }

    @Test
    @DisplayName("Should throw CustomerNotFoundException when deleting non-existing customer")
    void shouldThrowCustomerNotFoundExceptionWhenDeleting() {
        when(customerRepository.existsById(customerId)).thenReturn(false);
        assertThrows(CustomerNotFoundException.class, () -> customerService.deleteCustomer(customerId));
        verify(customerRepository).existsById(customerId);
        verify(customerRepository, never()).deleteById(any());
    }
}
