package io.github.nivaldosilva.bookstore.repositories;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import io.github.nivaldosilva.bookstore.entities.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, UUID> {

}
