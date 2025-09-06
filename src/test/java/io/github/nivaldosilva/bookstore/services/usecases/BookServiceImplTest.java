package io.github.nivaldosilva.bookstore.services.usecases;

import io.github.nivaldosilva.bookstore.dtos.common.PagedResponse;
import io.github.nivaldosilva.bookstore.dtos.request.BookRequest;
import io.github.nivaldosilva.bookstore.dtos.response.BookResponse;
import io.github.nivaldosilva.bookstore.entities.Author;
import io.github.nivaldosilva.bookstore.entities.Book;
import io.github.nivaldosilva.bookstore.enums.Genre;
import io.github.nivaldosilva.bookstore.exceptions.AuthorNotFoundException;
import io.github.nivaldosilva.bookstore.exceptions.BookNotFoundException;
import io.github.nivaldosilva.bookstore.exceptions.IsbnAlreadyExistsException;
import io.github.nivaldosilva.bookstore.repositories.AuthorRepository;
import io.github.nivaldosilva.bookstore.repositories.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("BookService Tests")
class BookServiceImplTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private AuthorRepository authorRepository;

    @InjectMocks
    private BookServiceImpl bookService;

    private Author author;
    private Book book;
    private BookRequest bookRequest;
    private UUID authorId;
    private UUID bookId;

    @BeforeEach
    void setUp() {
        authorId = UUID.randomUUID();
        bookId = UUID.randomUUID();

        author = Author.builder()
                .id(authorId)
                .name("J.K. Rowling")
                .biography("Author of Harry Potter series")
                .birthDate(LocalDate.of(1965, 7, 31))
                .nationality("British")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        book = Book.builder()
                .id(bookId)
                .isbn("978-0545010221")
                .title("Harry Potter and the Philosopher's Stone")
                .synopsis("A young wizard's journey begins")
                .genre(Genre.FANTASY)
                .publicationDate(LocalDate.of(1997, 6, 26))
                .price(BigDecimal.valueOf(29.99))
                .stockQuantity(100)
                .author(author)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        bookRequest = BookRequest.builder()
                .isbn("978-0545010221")
                .title("Harry Potter and the Philosopher's Stone")
                .synopsis("A young wizard's journey begins")
                .genre(Genre.FANTASY)
                .publicationDate(LocalDate.of(1997, 6, 26))
                .price(BigDecimal.valueOf(29.99))
                .stockQuantity(100)
                .authorId(authorId)
                .build();
    }

    @Test
    @DisplayName("Should create book successfully")
    void shouldCreateBookSuccessfully() {
        
        when(bookRepository.existsByIsbn(bookRequest.isbn())).thenReturn(false);
        when(authorRepository.findById(authorId)).thenReturn(Optional.of(author));
        when(bookRepository.save(any(Book.class))).thenReturn(book);

       
        BookResponse result = bookService.createBook(bookRequest);

       
        assertNotNull(result);
        assertEquals(book.getIsbn(), result.isbn());
        assertEquals(book.getTitle(), result.title());
        assertEquals(book.getGenre(), result.genre());
        assertEquals(book.getPrice(), result.price());

        verify(bookRepository).existsByIsbn(bookRequest.isbn());
        verify(authorRepository).findById(authorId);
        verify(bookRepository).save(any(Book.class));
    }

    @Test
    @DisplayName("Should throw IsbnAlreadyExistsException when ISBN already exists")
    void shouldThrowIsbnAlreadyExistsExceptionWhenIsbnExists() {
       
        when(bookRepository.existsByIsbn(bookRequest.isbn())).thenReturn(true);

        
        assertThrows(IsbnAlreadyExistsException.class, () -> bookService.createBook(bookRequest));

        verify(bookRepository).existsByIsbn(bookRequest.isbn());
        verify(authorRepository, never()).findById(any());
        verify(bookRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw AuthorNotFoundException when author not found")
    void shouldThrowAuthorNotFoundExceptionWhenAuthorNotFound() {
       
        when(bookRepository.existsByIsbn(bookRequest.isbn())).thenReturn(false);
        when(authorRepository.findById(authorId)).thenReturn(Optional.empty());

       
        assertThrows(AuthorNotFoundException.class, () -> bookService.createBook(bookRequest));

        verify(bookRepository).existsByIsbn(bookRequest.isbn());
        verify(authorRepository).findById(authorId);
        verify(bookRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should find book by ID successfully")
    void shouldFindBookByIdSuccessfully() {
     
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));

       
        BookResponse result = bookService.findBookById(bookId);

      
        assertNotNull(result);
        assertEquals(book.getId(), result.id());
        assertEquals(book.getTitle(), result.title());
        assertEquals(book.getIsbn(), result.isbn());

        verify(bookRepository).findById(bookId);
    }

    @Test
    @DisplayName("Should throw BookNotFoundException when book not found by ID")
    void shouldThrowBookNotFoundExceptionWhenBookNotFoundById() {
       
        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        
        assertThrows(BookNotFoundException.class, () -> bookService.findBookById(bookId));

        verify(bookRepository).findById(bookId);
    }

    @Test
    @DisplayName("Should find all books with pagination")
    void shouldFindAllBooksWithPagination() {
       
        Pageable pageable = PageRequest.of(0, 10);
        Page<Book> bookPage = new PageImpl<>(List.of(book), pageable, 1);

        when(bookRepository.findAll(ArgumentMatchers.<Specification<Book>>any(), eq(pageable))).thenReturn(bookPage);

       
        PagedResponse<BookResponse> result = bookService.findAllBooks(pageable, null, null);

       
        assertNotNull(result);
        assertEquals(1, result.content().size());
        assertEquals(1, result.totalElements());
        assertEquals(0, result.pageNumber());
        assertTrue(result.first());
        assertTrue(result.last());

        verify(bookRepository).findAll(ArgumentMatchers.<Specification<Book>>any(), eq(pageable));
    }

    @Test
    @DisplayName("Should find books filtered by genre")
    void shouldFindBooksFilteredByGenre() {
        
        Pageable pageable = PageRequest.of(0, 10);
        Page<Book> bookPage = new PageImpl<>(List.of(book), pageable, 1);

        when(bookRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(bookPage);

       
        PagedResponse<BookResponse> result = bookService.findAllBooks(pageable, Genre.FANTASY, null);

        
        assertNotNull(result);
        assertEquals(1, result.content().size());
        assertEquals(Genre.FANTASY, result.content().get(0).genre());

        verify(bookRepository).findAll(any(Specification.class), eq(pageable));
    }

    @Test
    @DisplayName("Should find books filtered by author")
    void shouldFindBooksFilteredByAuthor() {
        
        Pageable pageable = PageRequest.of(0, 10);
        Page<Book> bookPage = new PageImpl<>(List.of(book), pageable, 1);

        when(bookRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(bookPage);

       
        PagedResponse<BookResponse> result = bookService.findAllBooks(pageable, null, authorId);

        
        assertNotNull(result);
        assertEquals(1, result.content().size());
        assertEquals(authorId, result.content().get(0).authorId());

        verify(bookRepository).findAll(any(Specification.class), eq(pageable));
    }

    @Test
    @DisplayName("Should find books filtered by genre and author")
    void shouldFindBooksFilteredByGenreAndAuthor() {
        
        Pageable pageable = PageRequest.of(0, 10);
        Page<Book> bookPage = new PageImpl<>(List.of(book), pageable, 1);

        when(bookRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(bookPage);

       
        PagedResponse<BookResponse> result = bookService.findAllBooks(pageable, Genre.FANTASY, authorId);

       
        assertNotNull(result);
        assertEquals(1, result.content().size());
        assertEquals(Genre.FANTASY, result.content().get(0).genre());
        assertEquals(authorId, result.content().get(0).authorId());

        verify(bookRepository).findAll(any(Specification.class), eq(pageable));
    }

    @Test
    @DisplayName("Should return empty page when no books found")
    void shouldReturnEmptyPageWhenNoBooksFound() {
       
        Pageable pageable = PageRequest.of(0, 10);
        Page<Book> emptyPage = new PageImpl<>(List.of(), pageable, 0);

        when(bookRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(emptyPage);

       
        PagedResponse<BookResponse> result = bookService.findAllBooks(pageable, null, null);

        
        assertNotNull(result);
        assertEquals(0, result.content().size());
        assertEquals(0, result.totalElements());
        assertTrue(result.first());
        assertTrue(result.last());

        verify(bookRepository).findAll(any(Specification.class), eq(pageable));
    }

    @Test
    @DisplayName("Should update book successfully with different ISBN")
    void shouldUpdateBookSuccessfullyWithDifferentIsbn() {
       
        BookRequest updateRequest = new BookRequest(
                null,
                "978-0545010222", 
                "Updated Title",
                "Updated synopsis",
                Genre.SCIENCE_FICTION,
                LocalDate.of(2000, 1, 1),
                BigDecimal.valueOf(39.99),
                50,
                authorId
        );
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(bookRepository.existsByIsbn(updateRequest.isbn())).thenReturn(false);
        when(bookRepository.save(any(Book.class))).thenReturn(book);

       
        BookResponse result = bookService.updateBook(bookId, updateRequest);

        
        assertNotNull(result);

        verify(bookRepository).findById(bookId);
        verify(bookRepository).existsByIsbn(updateRequest.isbn());
        verify(authorRepository, never()).findById(any()); // Same author
        verify(bookRepository).save(any(Book.class));
    }

    @Test
    @DisplayName("Should update book with same ISBN successfully")
    void shouldUpdateBookWithSameIsbnSuccessfully() {
       
        BookRequest updateRequest = new BookRequest(
                null,
                book.getIsbn(), 
                "Updated Title",
                "Updated synopsis",
                Genre.SCIENCE_FICTION,
                LocalDate.of(2000, 1, 1),
                BigDecimal.valueOf(39.99),
                50,
                authorId
        );

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(bookRepository.save(any(Book.class))).thenReturn(book);

     
        BookResponse result = bookService.updateBook(bookId, updateRequest);

        assertNotNull(result);

        verify(bookRepository).findById(bookId);
        verify(bookRepository, never()).existsByIsbn(any()); 
        verify(authorRepository, never()).findById(any()); 
        verify(bookRepository).save(any(Book.class));
    }

    @Test
    @DisplayName("Should throw BookNotFoundException when updating non-existing book")
    void shouldThrowBookNotFoundExceptionWhenUpdatingNonExistingBook() {
       
        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        
        assertThrows(BookNotFoundException.class, () -> bookService.updateBook(bookId, bookRequest));

        verify(bookRepository).findById(bookId);
        verify(bookRepository, never()).existsByIsbn(any());
        verify(bookRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw IsbnAlreadyExistsException when updating with existing ISBN")
    void shouldThrowIsbnAlreadyExistsExceptionWhenUpdatingWithExistingIsbn() {
        
        BookRequest updateRequest = new BookRequest(
                null,
                "978-0545010222", 
                "Updated Title",
                "Updated synopsis",
                Genre.SCIENCE_FICTION,
                LocalDate.of(2000, 1, 1),
                BigDecimal.valueOf(39.99),
                50,
                authorId
        );

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(bookRepository.existsByIsbn(updateRequest.isbn())).thenReturn(true);

       
        assertThrows(IsbnAlreadyExistsException.class, () -> bookService.updateBook(bookId, updateRequest));

        verify(bookRepository).findById(bookId);
        verify(bookRepository).existsByIsbn(updateRequest.isbn());
        verify(bookRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should update book with new author successfully")
    void shouldUpdateBookWithNewAuthorSuccessfully() {
        // Given
        UUID newAuthorId = UUID.randomUUID();
        Author newAuthor = Author.builder()
                .id(newAuthorId)
                .name("George Orwell")
                .biography("Author of 1984 and Animal Farm")
                .birthDate(LocalDate.of(1903, 6, 25))
                .build();

        BookRequest updateRequest = new BookRequest(
                null,
                "978-0545010222", 
                "Updated Title",
                "Updated synopsis",
                Genre.SCIENCE_FICTION,
                LocalDate.of(2000, 1, 1),
                BigDecimal.valueOf(39.99),
                50,
                newAuthorId 
        );

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(bookRepository.existsByIsbn(updateRequest.isbn())).thenReturn(false);
        when(authorRepository.findById(newAuthorId)).thenReturn(Optional.of(newAuthor));
        when(bookRepository.save(any(Book.class))).thenReturn(book);

        
        BookResponse result = bookService.updateBook(bookId, updateRequest);

        
        assertNotNull(result);

        verify(bookRepository).findById(bookId);
        verify(bookRepository).existsByIsbn(updateRequest.isbn());
        verify(authorRepository).findById(newAuthorId);
        verify(bookRepository).save(any(Book.class));
    }

    @Test
    @DisplayName("Should update book with same author successfully")
    void shouldUpdateBookWithSameAuthorSuccessfully() {
      
        BookRequest updateRequest = new BookRequest(
                null,
                "978-0545010222",
                "Updated Title",
                "Updated synopsis",
                Genre.SCIENCE_FICTION,
                LocalDate.of(2000, 1, 1),
                BigDecimal.valueOf(39.99),
                50,
                authorId 
        );

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(bookRepository.existsByIsbn(updateRequest.isbn())).thenReturn(false);
        when(bookRepository.save(any(Book.class))).thenReturn(book);

        
        BookResponse result = bookService.updateBook(bookId, updateRequest);

       
        assertNotNull(result);

        verify(bookRepository).findById(bookId);
        verify(bookRepository).existsByIsbn(updateRequest.isbn());
        verify(authorRepository, never()).findById(any()); 
        verify(bookRepository).save(any(Book.class));
    }

    @Test
    @DisplayName("Should throw AuthorNotFoundException when updating with non-existing author")
    void shouldThrowAuthorNotFoundExceptionWhenUpdatingWithNonExistingAuthor() {
        // Given
        UUID newAuthorId = UUID.randomUUID();
        BookRequest updateRequest = new BookRequest(
                null,
                "978-0545010222", 
                "Updated Title",
                "Updated synopsis",
                Genre.SCIENCE_FICTION,
                LocalDate.of(2000, 1, 1),
                BigDecimal.valueOf(39.99),
                50,
                newAuthorId
        );

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(bookRepository.existsByIsbn(updateRequest.isbn())).thenReturn(false);
        when(authorRepository.findById(newAuthorId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(AuthorNotFoundException.class, () -> bookService.updateBook(bookId, updateRequest));

        verify(bookRepository).findById(bookId);
        verify(bookRepository).existsByIsbn(updateRequest.isbn());
        verify(authorRepository).findById(newAuthorId);
        verify(bookRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should delete book successfully")
    void shouldDeleteBookSuccessfully() {
        // Given
        when(bookRepository.existsById(bookId)).thenReturn(true);

        // When
        bookService.deleteBook(bookId);

        // Then
        verify(bookRepository).existsById(bookId);
        verify(bookRepository).deleteById(bookId);
    }

    @Test
    @DisplayName("Should throw BookNotFoundException when deleting non-existing book")
    void shouldThrowBookNotFoundExceptionWhenDeletingNonExistingBook() {
        // Given
        when(bookRepository.existsById(bookId)).thenReturn(false);

        // When & Then
        assertThrows(BookNotFoundException.class, () -> bookService.deleteBook(bookId));

        verify(bookRepository).existsById(bookId);
        verify(bookRepository, never()).deleteById(bookId);
    }
}