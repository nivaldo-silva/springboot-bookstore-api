package io.github.nivaldosilva.bookstore.services.interfaces;

import java.util.List;
import java.util.UUID;
import io.github.nivaldosilva.bookstore.dtos.request.OrderRequest;
import io.github.nivaldosilva.bookstore.dtos.response.OrderResponse;
import io.github.nivaldosilva.bookstore.enums.OrderStatus;

public interface OrderService {

    OrderResponse createOrder(OrderRequest orderRequest);

    OrderResponse findOrderById(UUID id);

    List<OrderResponse> findAllOrders();

    OrderResponse updateOrderStatus(UUID id, OrderStatus newStatus);

    void deleteOrder(UUID id);
}
