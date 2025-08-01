package io.github.nivaldosilva.bookstore.repositories;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import io.github.nivaldosilva.bookstore.entities.Book;

public interface BookRepository extends JpaRepository<Book, UUID> {

    Optional<Book> findByIsbn(String isbn);

    boolean existsByIsbn(String isbn);

    Page<Book> findAll(Specification<Book> spec, Pageable pageable);

}
