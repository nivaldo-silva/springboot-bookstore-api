package io.github.nivaldosilva.bookstore.repositories;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import io.github.nivaldosilva.bookstore.entities.Customer;

public interface CustomerRepository extends JpaRepository<Customer, UUID> {

    Optional<Customer> findByEmail(String email);

    boolean existsByEmail(String email);

}
