package io.github.nivaldosilva.bookstore.services.interfaces;

import java.util.List;
import java.util.UUID;
import io.github.nivaldosilva.bookstore.dtos.request.AuthorRequest;
import io.github.nivaldosilva.bookstore.dtos.response.AuthorResponse;

public interface AuthorService {

    AuthorResponse createAuthor(AuthorRequest request);

    AuthorResponse findAuthorById(UUID id);

    List<AuthorResponse> findAllAuthors();

    AuthorResponse updateAuthor(UUID id, AuthorRequest request);

    void deleteAuthor(UUID id);

}
