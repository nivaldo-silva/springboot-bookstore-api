package io.github.nivaldosilva.bookstore.repositories;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import io.github.nivaldosilva.bookstore.entities.Author;

public interface AuthorRepository extends JpaRepository<Author, UUID> {

    boolean existsByName(String name);

}
