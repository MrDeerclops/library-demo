package com.musiienko.library.service;

import com.musiienko.library.model.BookUpdateRequest;
import com.musiienko.library.model.BookViewResponse;
import com.musiienko.library.model.UpdateResponse;

import java.util.List;

public interface BookService {
    List<BookViewResponse> getAllBooks();

    List<BookViewResponse> searchBooks(String match);

    BookViewResponse getBookById(Long id);

    UpdateResponse createBook(BookUpdateRequest request);

    UpdateResponse updateBookById(Long id, BookUpdateRequest request);

    UpdateResponse deleteBookById(Long id);

    UpdateResponse borrowBookById(Long id);

    UpdateResponse returnBookById(Long id);
}
