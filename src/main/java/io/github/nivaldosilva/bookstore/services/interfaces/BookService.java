package io.github.nivaldosilva.bookstore.services.interfaces;

import java.util.UUID;
import org.springframework.data.domain.Pageable;
import io.github.nivaldosilva.bookstore.dtos.common.PagedResponse;
import io.github.nivaldosilva.bookstore.dtos.request.BookRequest;
import io.github.nivaldosilva.bookstore.dtos.response.BookResponse;
import io.github.nivaldosilva.bookstore.enums.Genre;

public interface BookService {

    BookResponse createBook(BookRequest request);

    BookResponse findBookById(UUID id);

    PagedResponse<BookResponse> findAllBooks(Pageable pageable, Genre genre, UUID authorId);

    BookResponse updateBook(UUID id, BookRequest request);

    void deleteBook(UUID id);

}
