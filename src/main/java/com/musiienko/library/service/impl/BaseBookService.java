package com.musiienko.library.service.impl;

import com.musiienko.library.constant.Constant;
import com.musiienko.library.entity.Book;
import com.musiienko.library.entity.User;
import com.musiienko.library.exception.BookAlreadyBorrowedException;
import com.musiienko.library.exception.BookHasDifferentBorrowerException;
import com.musiienko.library.exception.BookNotBorrowedException;
import com.musiienko.library.exception.BookNotFoundException;
import com.musiienko.library.model.BookUpdateRequest;
import com.musiienko.library.model.BookViewResponse;
import com.musiienko.library.model.UpdateResponse;
import com.musiienko.library.repository.BookRepository;
import com.musiienko.library.service.AuthService;
import com.musiienko.library.service.BookService;
import com.musiienko.library.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class BaseBookService implements BookService {

    BookRepository bookRepository;
    UserService userService;
    AuthService authService;
    ModelMapper modelMapper;

    @Override
    public List<BookViewResponse> getAllBooks() {
        log.info("READ request for all books");
        return bookRepository.findAll().stream()
                .map(book -> modelMapper.map(book, BookViewResponse.class)).toList();
    }

    @Override
    public List<BookViewResponse> searchBooks(String match) {
        log.info("READ request for match {}", match);
        return bookRepository.findByAuthorOrTitleMatch(match).stream()
                .map(book -> modelMapper.map(book, BookViewResponse.class)).toList();
    }

    @Override
    public BookViewResponse getBookById(Long id) {
        log.info("READ request for book, ID: {}", id);
        return modelMapper.map(bookRepository.findById(id).orElseThrow(() -> new BookNotFoundException(id)),
                BookViewResponse.class);
    }

    @Override
    @Transactional
    public UpdateResponse createBook(BookUpdateRequest request) {
        log.info("CREATE request for book: {}", request);
        Book createdBook = bookRepository.save(
                modelMapper.map(request, Book.class)
        );
        return UpdateResponse.builder().id(createdBook.getId()).status(Constant.BOOK_CREATED_MESSAGE).build();
    }

    @Override
    @Transactional
    public UpdateResponse updateBookById(Long id, BookUpdateRequest request) {
        log.info("UPDATE request for book, ID: {}, update: {}", id, request);
        Book book = bookRepository.findById(id).orElseThrow(() -> new BookNotFoundException(id));
        modelMapper.map(request, book);
        bookRepository.save(book);
        return UpdateResponse.builder().id(id).status(Constant.BOOK_UPDATED_MESSAGE).build();
    }

    @Override
    @Transactional
    public UpdateResponse deleteBookById(Long id) {
        log.info("DELETE request for book, ID: {}", id);
        Book book = bookRepository.findById(id).orElseThrow(() -> new BookNotFoundException(id));
        bookRepository.delete(book);
        return UpdateResponse.builder().id(id).status(Constant.BOOK_DELETED_MESSAGE).build();
    }

    @Override
    @Transactional
    public UpdateResponse borrowBookById(Long id) {
        User user = authService.getCurrentUser();
        log.info("BORROW request for book, ID: {}, by user, ID: {}", id, user.getId());
        Book book = bookRepository.findById(id).orElseThrow(() -> new BookNotFoundException(id));
        if (Objects.nonNull(book.getUser())) {
            throw new BookAlreadyBorrowedException(id);
        }
        user.getBorrowedBooks().add(book);
        book.setUser(user);
        userService.save(user);
        return UpdateResponse.builder().id(id).status(Constant.BOOK_BORROWED_MESSAGE).build();
    }

    @Override
    @Transactional
    public UpdateResponse returnBookById(Long id) {
        User user = authService.getCurrentUser();
        log.info("RETURN request for book, ID: {}, by user, ID: {}", id, user.getId());
        Book book = bookRepository.findById(id).orElseThrow(() -> new BookNotFoundException(id));
        if (Objects.isNull(book.getUser())) {
            throw new BookNotBorrowedException(id);
        }
        if (!user.getUsername().equals(book.getUser().getUsername())) {
            throw new BookHasDifferentBorrowerException(id);
        }
        book.setUser(null);
        user.getBorrowedBooks().remove(book);
        userService.save(user);
        return UpdateResponse.builder().id(id).status(Constant.BOOK_RETURNED_MESSAGE).build();
    }

}
