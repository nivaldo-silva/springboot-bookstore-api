package io.github.nivaldosilva.bookstore.services.usecases;

import io.github.nivaldosilva.bookstore.dtos.request.OrderItemRequest;
import io.github.nivaldosilva.bookstore.dtos.request.OrderRequest;
import io.github.nivaldosilva.bookstore.dtos.response.OrderResponse;
import io.github.nivaldosilva.bookstore.entities.Book;
import io.github.nivaldosilva.bookstore.entities.Customer;
import io.github.nivaldosilva.bookstore.entities.Order;
import io.github.nivaldosilva.bookstore.entities.OrderItem;
import io.github.nivaldosilva.bookstore.enums.OrderStatus;
import io.github.nivaldosilva.bookstore.exceptions.BookNotFoundException;
import io.github.nivaldosilva.bookstore.exceptions.CustomerNotFoundException;
import io.github.nivaldosilva.bookstore.exceptions.InsufficientStockException;
import io.github.nivaldosilva.bookstore.exceptions.OrderNotFoundException;
import io.github.nivaldosilva.bookstore.repositories.BookRepository;
import io.github.nivaldosilva.bookstore.repositories.CustomerRepository;
import io.github.nivaldosilva.bookstore.repositories.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("OrderService Tests")
class OrderServiceImplTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private OrderServiceImpl orderService;

    private UUID customerId;
    private String customerEmail;
    private Customer customer;
    private UUID bookId;
    private String bookIsbn;
    private Book book;
    private OrderRequest orderRequest;
    private Order order;
    private UUID orderId;

    @BeforeEach
    void setUp() {
        customerId = UUID.randomUUID();
        customerEmail = "customer@example.com";
        bookId = UUID.randomUUID();
        bookIsbn = "978-0545010221";
        orderId = UUID.randomUUID();

        customer = Customer.builder()
                .id(customerId)
                .email(customerEmail)
                .build();
        book = Book.builder()
                .id(bookId)
                .isbn(bookIsbn)
                .title("Harry Potter")
                .price(BigDecimal.valueOf(29.99))
                .stockQuantity(100)
                .build();
        orderRequest = OrderRequest.builder()
                .customerEmail(customerEmail)
                .items(List.of(new OrderItemRequest(bookIsbn, 2)))
                .build();
        order = Order.builder()
                .id(orderId)
                .customer(customer)
                .totalAmount(BigDecimal.valueOf(59.98))
                .status(OrderStatus.PENDING)
                .items(List.of(
                        OrderItem.builder()
                                .book(book)
                                .quantity(2)
                                .unitPrice(book.getPrice())
                                .totalPrice(BigDecimal.valueOf(59.98))
                                .build()))
                .build();
    }

    @Test
    @DisplayName("Should create order successfully")
    void shouldCreateOrderSuccessfully() {
        when(customerRepository.findByEmail(customerEmail)).thenReturn(Optional.of(customer));
        when(bookRepository.findByIsbn(bookIsbn)).thenReturn(Optional.of(book));
        when(orderRepository.save(any(Order.class))).thenReturn(order);
        OrderResponse result = orderService.createOrder(orderRequest);
        assertNotNull(result);
        assertEquals(order.getCustomer().getEmail(), result.customerEmail());
        assertEquals(order.getTotalAmount(), result.totalAmount());
        assertEquals(OrderStatus.PENDING, result.status());
        verify(customerRepository).findByEmail(customerEmail);
        verify(bookRepository).findByIsbn(bookIsbn);
        verify(orderRepository).save(any(Order.class));
        verify(bookRepository).save(any(Book.class));
    }

    @Test
    @DisplayName("Should throw CustomerNotFoundException when creating order for non-existing customer")
    void shouldThrowCustomerNotFoundExceptionWhenCreatingOrder() {
        when(customerRepository.findByEmail(customerEmail)).thenReturn(Optional.empty());
        assertThrows(CustomerNotFoundException.class, () -> orderService.createOrder(orderRequest));
        verify(customerRepository).findByEmail(customerEmail);
        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when order has no items")
    void shouldThrowIllegalArgumentExceptionWhenNoItems() {
        OrderRequest emptyOrderRequest = OrderRequest.builder().customerEmail(customerEmail).build();
        when(customerRepository.findByEmail(customerEmail)).thenReturn(Optional.of(customer));
        assertThrows(IllegalArgumentException.class, () -> orderService.createOrder(emptyOrderRequest));
        verify(customerRepository).findByEmail(customerEmail);
        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    @DisplayName("Should throw BookNotFoundException when a book in order does not exist")
    void shouldThrowBookNotFoundExceptionWhenCreatingOrder() {
        when(customerRepository.findByEmail(customerEmail)).thenReturn(Optional.of(customer));
        when(bookRepository.findByIsbn(bookIsbn)).thenReturn(Optional.empty());
        assertThrows(BookNotFoundException.class, () -> orderService.createOrder(orderRequest));
        verify(customerRepository).findByEmail(customerEmail);
        verify(bookRepository).findByIsbn(bookIsbn);
        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    @DisplayName("Should throw InsufficientStockException when stock is not enough")
    void shouldThrowInsufficientStockException() {
        Book bookWithLowStock = Book.builder()
                .id(bookId)
                .isbn(bookIsbn)
                .stockQuantity(1)
                .build();
        when(customerRepository.findByEmail(customerEmail)).thenReturn(Optional.of(customer));
        when(bookRepository.findByIsbn(bookIsbn)).thenReturn(Optional.of(bookWithLowStock));
        assertThrows(InsufficientStockException.class, () -> orderService.createOrder(orderRequest));
        verify(customerRepository).findByEmail(customerEmail);
        verify(bookRepository).findByIsbn(bookIsbn);
        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    @DisplayName("Should find order by ID successfully")
    void shouldFindOrderByIdSuccessfully() {
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        OrderResponse result = orderService.findOrderById(orderId);
        assertNotNull(result);
        assertEquals(order.getId(), result.id());
        assertEquals(order.getTotalAmount(), result.totalAmount());
        verify(orderRepository).findById(orderId);
    }

    @Test
    @DisplayName("Should throw OrderNotFoundException when order not found by ID")
    void shouldThrowOrderNotFoundExceptionWhenNotFound() {
        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());
        assertThrows(OrderNotFoundException.class, () -> orderService.findOrderById(orderId));
        verify(orderRepository).findById(orderId);
    }

    @Test
    @DisplayName("Should find all orders successfully")
    void shouldFindAllOrdersSuccessfully() {
        List<Order> orders = List.of(order);
        when(orderRepository.findAll()).thenReturn(orders);
        List<OrderResponse> result = orderService.findAllOrders();
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(order.getId(), result.get(0).id());
        verify(orderRepository).findAll();
    }

    @Test
    @DisplayName("Should update order status successfully")
    void shouldUpdateOrderStatusSuccessfully() {
        OrderStatus newStatus = OrderStatus.SHIPPED;
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(orderRepository.save(any(Order.class))).thenReturn(order);
        OrderResponse result = orderService.updateOrderStatus(orderId, newStatus);
        assertNotNull(result);
        assertEquals(newStatus, result.status());
        verify(orderRepository).findById(orderId);
        verify(orderRepository).save(any(Order.class));
    }

    @Test
    @DisplayName("Should throw OrderNotFoundException when updating status of non-existing order")
    void shouldThrowOrderNotFoundExceptionWhenUpdatingStatus() {
        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());
        assertThrows(OrderNotFoundException.class,
                () -> orderService.updateOrderStatus(orderId, OrderStatus.CANCELLED));
        verify(orderRepository).findById(orderId);
        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    @DisplayName("Should delete order successfully")
    void shouldDeleteOrderSuccessfully() {
        when(orderRepository.existsById(orderId)).thenReturn(true);
        orderService.deleteOrder(orderId);
        verify(orderRepository).existsById(orderId);
        verify(orderRepository).deleteById(orderId);
    }

    @Test
    @DisplayName("Should throw OrderNotFoundException when deleting non-existing order")
    void shouldThrowOrderNotFoundExceptionWhenDeleting() {
        when(orderRepository.existsById(orderId)).thenReturn(false);
        assertThrows(OrderNotFoundException.class, () -> orderService.deleteOrder(orderId));
        verify(orderRepository).existsById(orderId);
        verify(orderRepository, never()).deleteById(any());
    }
}
