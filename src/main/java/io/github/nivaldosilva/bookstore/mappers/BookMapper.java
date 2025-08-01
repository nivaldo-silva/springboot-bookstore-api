package io.github.nivaldosilva.bookstore.mappers;

import io.github.nivaldosilva.bookstore.dtos.request.BookRequest;
import io.github.nivaldosilva.bookstore.dtos.response.BookResponse;
import io.github.nivaldosilva.bookstore.entities.Author;
import io.github.nivaldosilva.bookstore.entities.Book;

public class BookMapper {

    public static Book toEntity(BookRequest request, Author author) {
        return Book.builder()
                .id(request.id())
                .isbn(request.isbn())
                .title(request.title())
                .synopsis(request.synopsis())
                .genre(request.genre())
                .publicationDate(request.publicationDate())
                .price(request.price())
                .stockQuantity(request.stockQuantity())
                .author(author)
                .build();
    }

    public static BookResponse toResponse(Book book) {
        
        String authorName = (book.getAuthor() != null) ? book.getAuthor().getName() : null;
        return BookResponse.builder()
                .id(book.getId())
                .isbn(book.getIsbn())
                .title(book.getTitle())
                .synopsis(book.getSynopsis())
                .genre(book.getGenre())
                .publicationDate(book.getPublicationDate())
                .price(book.getPrice())
                .stockQuantity(book.getStockQuantity())
                .authorId((book.getAuthor() != null) ? book.getAuthor().getId() : null)
                .authorName(authorName)
                .createdAt(book.getCreatedAt())
                .updatedAt(book.getUpdatedAt())
                .build();
    }

}
