package com.musiienko.library.controller;

import com.musiienko.library.model.BookUpdateRequest;
import com.musiienko.library.model.BookViewResponse;
import com.musiienko.library.model.UpdateResponse;
import com.musiienko.library.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@RequestMapping("/api/books")
@Tag(name = "Library")
public class LibraryController {

    BookService bookService;

    @Operation(summary = "Get all the books in the library catalog")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<BookViewResponse>> allBooks() {
        return ResponseEntity.ok(bookService.getAllBooks());
    }

    @Operation(summary = "Search books by string, that matches (as a substring) either title or author of the book")
    @GetMapping(value = "/search", params = "match", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<BookViewResponse>> searchBooks(@RequestParam("match") String match) {
        return ResponseEntity.ok(bookService.searchBooks(match));
    }
    @Operation(summary = "Get book by id")
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BookViewResponse> getBook(@PathVariable Long id) {
        return ResponseEntity.ok(bookService.getBookById(id));
    }
    @Operation(summary = "Add new book to the catalog")
    @SecurityRequirement(name = "ROLE_LIBRARIAN")
    @PostMapping(value = "/new", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UpdateResponse> addBook(@RequestBody @Valid BookUpdateRequest request) {
        return ResponseEntity.ok(bookService.createBook(request));
    }
    @Operation(summary = "Edit book by id")
    @SecurityRequirement(name = "ROLE_LIBRARIAN")
    @PutMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UpdateResponse> editBook(@PathVariable Long id, @RequestBody @Valid BookUpdateRequest request) {
        return ResponseEntity.ok(bookService.updateBookById(id, request));
    }
    @Operation(summary = "Delete book by id")
    @SecurityRequirement(name = "ROLE_LIBRARIAN")
    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UpdateResponse> deleteBook(@PathVariable Long id) {
        return ResponseEntity.ok(bookService.deleteBookById(id));
    }
    @Operation(summary = "Borrow the book by id")
    @SecurityRequirement(name = "ROLE_USER")
    @PutMapping(value = "/{id}/borrow", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UpdateResponse> borrowBook(@PathVariable Long id) {
        return ResponseEntity.ok(bookService.borrowBookById(id));
    }

    @Operation(summary = "Return the book by id")
    @SecurityRequirement(name = "ROLE_USER")
    @PutMapping(value = "/{id}/return", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UpdateResponse> returnBook(@PathVariable Long id) {
        return ResponseEntity.ok(bookService.returnBookById(id));
    }
}
