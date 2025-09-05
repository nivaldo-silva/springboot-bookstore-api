package io.github.nivaldosilva.bookstore.services.usecases;

import io.github.nivaldosilva.bookstore.dtos.request.AuthorRequest;
import io.github.nivaldosilva.bookstore.dtos.response.AuthorResponse;
import io.github.nivaldosilva.bookstore.entities.Author;
import io.github.nivaldosilva.bookstore.exceptions.AuthorNameAlreadyExistsException;
import io.github.nivaldosilva.bookstore.exceptions.AuthorNotFoundException;
import io.github.nivaldosilva.bookstore.mappers.AuthorMapper;
import io.github.nivaldosilva.bookstore.repositories.AuthorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("AuthorService Tests")
class AuthorServiceImplTest {

    @Mock
    private AuthorRepository authorRepository;

    @InjectMocks
    private AuthorServiceImpl authorService;

    private UUID authorId;
    private AuthorRequest authorRequest;
    private Author author;

    @BeforeEach
    void setUp() {
        authorId = UUID.randomUUID();
        authorRequest = AuthorRequest.builder()
                .name("J.K. Rowling")
                .biography("Author of Harry Potter series")
                .birthDate(LocalDate.of(1965, 7, 31))
                .nationality("British")
                .build();
        author = Author.builder()
                .id(authorId)
                .name("J.K. Rowling")
                .biography("Author of Harry Potter series")
                .birthDate(LocalDate.of(1965, 7, 31))
                .nationality("British")
                .build();
    }

    @Test
    @DisplayName("Should create author successfully")
    void shouldCreateAuthorSuccessfully() {
        when(authorRepository.existsByName(authorRequest.name())).thenReturn(false);
        when(authorRepository.save(any(Author.class))).thenReturn(author);
        AuthorResponse result = authorService.createAuthor(authorRequest);
        assertNotNull(result);
        assertEquals(author.getName(), result.name());
        verify(authorRepository).existsByName(authorRequest.name());
        verify(authorRepository).save(any(Author.class));
    }

    @Test
    @DisplayName("Should throw AuthorNameAlreadyExistsException when name already exists")
    void shouldThrowAuthorNameAlreadyExistsException() {
        when(authorRepository.existsByName(authorRequest.name())).thenReturn(true);
        assertThrows(AuthorNameAlreadyExistsException.class, () -> authorService.createAuthor(authorRequest));
        verify(authorRepository).existsByName(authorRequest.name());
        verify(authorRepository, never()).save(any(Author.class));
    }

    @Test
    @DisplayName("Should find author by ID successfully")
    void shouldFindAuthorByIdSuccessfully() {
        when(authorRepository.findById(authorId)).thenReturn(Optional.of(author));
        AuthorResponse result = authorService.findAuthorById(authorId);
        assertNotNull(result);
        assertEquals(author.getId(), result.id());
        assertEquals(author.getName(), result.name());
        verify(authorRepository).findById(authorId);
    }

    @Test
    @DisplayName("Should throw AuthorNotFoundException when author not found by ID")
    void shouldThrowAuthorNotFoundExceptionWhenNotFound() {
        when(authorRepository.findById(authorId)).thenReturn(Optional.empty());
        assertThrows(AuthorNotFoundException.class, () -> authorService.findAuthorById(authorId));
        verify(authorRepository).findById(authorId);
    }

    @Test
    @DisplayName("Should find all authors successfully")
    void shouldFindAllAuthorsSuccessfully() {
        List<Author> authors = List.of(author);
        when(authorRepository.findAll()).thenReturn(authors);
        List<AuthorResponse> result = authorService.findAllAuthors();
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(authors.get(0).getName(), result.get(0).name());
        verify(authorRepository).findAll();
    }

    @Test
    @DisplayName("Should update author successfully with different name")
    void shouldUpdateAuthorSuccessfullyWithDifferentName() {
        AuthorRequest updateRequest = AuthorRequest.builder()
                .name("J.K. Rowling")
                .nationality("British")
                .birthDate(LocalDate.of(1965, 7, 31))
                .biography("Updated biography")
                .build();
        Author updatedAuthor = AuthorMapper.toEntity(updateRequest);
        when(authorRepository.findById(authorId)).thenReturn(Optional.of(author));
        when(authorRepository.existsByName(updateRequest.name())).thenReturn(false);
        when(authorRepository.save(any(Author.class))).thenReturn(updatedAuthor);
        AuthorResponse result = authorService.updateAuthor(authorId, updateRequest);
        assertNotNull(result);
        assertEquals(updateRequest.name(), result.name());
        verify(authorRepository).findById(authorId);
        verify(authorRepository).existsByName(updateRequest.name());
        verify(authorRepository).save(any(Author.class));
    }

    

    @Test
    @DisplayName("Should update author with same name successfully")
    void shouldUpdateAuthorWithSameNameSuccessfully() {
        AuthorRequest updateRequest = AuthorRequest.builder()
                .name("J.K. Rowling")
                .nationality("British")
                .birthDate(LocalDate.of(1965, 7, 31))
                .biography("Updated biography")
                .build();
        when(authorRepository.findById(authorId)).thenReturn(Optional.of(author));
        when(authorRepository.save(any(Author.class))).thenReturn(author);
        AuthorResponse result = authorService.updateAuthor(authorId, updateRequest);
        assertNotNull(result);
        assertEquals(updateRequest.name(), result.name());
        verify(authorRepository).findById(authorId);       
        verify(authorRepository).save(any(Author.class));
    }

    @Test
    @DisplayName("Should throw AuthorNotFoundException when updating non-existing author")
    void shouldThrowAuthorNotFoundExceptionWhenUpdating() {
        when(authorRepository.findById(authorId)).thenReturn(Optional.empty());
        assertThrows(AuthorNotFoundException.class, () -> authorService.updateAuthor(authorId, authorRequest));
        verify(authorRepository).findById(authorId);
        verify(authorRepository, never()).save(any(Author.class));
    }

    @Test
    @DisplayName("Should throw AuthorNameAlreadyExistsException when updating with existing name")
    void shouldThrowAuthorNameAlreadyExistsExceptionWhenUpdating() {
        AuthorRequest updateRequest = AuthorRequest.builder()
                .name("Another Author")
                .nationality("Another nationality")
                .birthDate(LocalDate.now())
                .biography("Another bio")
                .build();
        when(authorRepository.findById(authorId)).thenReturn(Optional.of(author));
        when(authorRepository.existsByName(updateRequest.name())).thenReturn(true);
        assertThrows(AuthorNameAlreadyExistsException.class, () -> authorService.updateAuthor(authorId, updateRequest));
        verify(authorRepository).findById(authorId);
        verify(authorRepository).existsByName(updateRequest.name());
        verify(authorRepository, never()).save(any(Author.class));
    }

    @Test
    @DisplayName("Should delete author successfully")
    void shouldDeleteAuthorSuccessfully() {
        when(authorRepository.existsById(authorId)).thenReturn(true);
        authorService.deleteAuthor(authorId);
        verify(authorRepository).existsById(authorId);
        verify(authorRepository).deleteById(authorId);
    }

    @Test
    @DisplayName("Should throw AuthorNotFoundException when deleting non-existing author")
    void shouldThrowAuthorNotFoundExceptionWhenDeleting() {
        when(authorRepository.existsById(authorId)).thenReturn(false);
        assertThrows(AuthorNotFoundException.class, () -> authorService.deleteAuthor(authorId));
        verify(authorRepository).existsById(authorId);
        verify(authorRepository, never()).deleteById(any());
    }
}