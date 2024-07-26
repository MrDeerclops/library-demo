package com.musiienko.library;

import com.musiienko.library.constant.Constant;
import com.musiienko.library.entity.Book;
import com.musiienko.library.entity.Category;
import com.musiienko.library.entity.Role;
import com.musiienko.library.entity.User;
import com.musiienko.library.exception.BookAlreadyBorrowedException;
import com.musiienko.library.exception.BookHasDifferentBorrowerException;
import com.musiienko.library.exception.BookNotBorrowedException;
import com.musiienko.library.exception.BookNotFoundException;
import com.musiienko.library.model.BookViewResponse;
import com.musiienko.library.model.UpdateResponse;
import com.musiienko.library.repository.BookRepository;
import com.musiienko.library.service.AuthService;
import com.musiienko.library.service.UserService;
import com.musiienko.library.service.impl.BaseBookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {

    private static final Long BOOK_ID1 = 1L;
    private static final Long BOOK_ID2 = 2L;
    private static final UpdateResponse BOOK1_BORROW_SUCCESS_RESPONSE = UpdateResponse.builder()
            .status(Constant.BOOK_BORROWED_MESSAGE).id(BOOK_ID1).build();
    private static final UpdateResponse BOOK2_RETURN_SUCCESS_RESPONSE = UpdateResponse.builder()
            .status(Constant.BOOK_RETURNED_MESSAGE).id(BOOK_ID2).build();
    private static User USER1;
    private static User USER2;
    private static Book BOOK1;
    private static Book BOOK2;
    private static BookViewResponse BOOK_DTO1;
    private static BookViewResponse BOOK_DTO2;
    @Mock
    AuthService authService;
    @Mock
    UserService userService;
    @Mock
    BookRepository bookRepository;
    @Mock
    ModelMapper modelMapper;
    @InjectMocks
    BaseBookService bookService;

    @BeforeEach
    void init() {
        USER1 = new User(1L, "user1", "$2y$10$frt3n1gViWmQ/SU9m8IZ.uGextZ8g63QdWOCtRzRRubwU1mOVgWvm",
                Role.ROLE_USER, new ArrayList<>());
        USER2 = new User(2L, "user2", "$2y$10$u/RGz7bmVnumeSjN4thC5e/F1TkEh.MZn7I3nsovcrV/oE7E51zWG",
                Role.ROLE_USER, new ArrayList<>());
        USER1.getBorrowedBooks().add(BOOK2);
        BOOK1 = new Book(1L, "9781848940871", "IT",
                "Stephen King", Category.HORROR, null);
        BOOK2 = new Book(2L, "9780553801477", "A Dance with Dragons",
                "George R.R. Martin", Category.FANTASY, USER1);
        BOOK_DTO1 = new BookViewResponse(1L, "9781848940871", "IT",
                "Stephen King", "Horror", false);
        BOOK_DTO2 = new BookViewResponse(2L, "9780553801477", "A Dance with Dragons",
                "George R.R. Martin", "Fantasy", true);

    }

    @Test
    @DisplayName("WHEN repository returns empty Optional SHOULD raise BookNotFoundException")
    void whenEmptyOptionalFromRepoThenRaiseException() {
        when(bookRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThatThrownBy(() -> bookService.getBookById(BOOK_ID1))
                .isInstanceOf(BookNotFoundException.class);
    }

    @Test
    @DisplayName("WHEN repository returns entity SHOULD return correct DTO")
    void whenRepoReturnsEntityThenReturnDto() {
        when(bookRepository.findById(BOOK_ID1)).thenReturn(Optional.of(BOOK1));
        when(modelMapper.map(BOOK1, BookViewResponse.class)).thenReturn(BOOK_DTO1);
        assertThat(bookService.getBookById(BOOK_ID1)).isEqualTo(BOOK_DTO1);
    }

    @Test
    @DisplayName("WHEN repository returns list SHOULD return list of correct DTOs")
    void whenRepoReturnsListThenReturnDtoList() {
        when(bookRepository.findAll()).thenReturn(List.of(BOOK1, BOOK2));
        when(modelMapper.map(BOOK1, BookViewResponse.class)).thenReturn(BOOK_DTO1);
        when(modelMapper.map(BOOK2, BookViewResponse.class)).thenReturn(BOOK_DTO2);
        assertThat(bookService.getAllBooks()).containsExactly(BOOK_DTO1, BOOK_DTO2);
    }

    @Test
    @DisplayName("WHEN user tries to return a book that is borrowed by them SHOULD return successful response")
    void whenUserReturnsValidBookShouldReturnSuccess() {
        when(bookRepository.findById(BOOK_ID2)).thenReturn(Optional.of(BOOK2));
        when(authService.getCurrentUser()).thenReturn(USER1);
        assertThat(bookService.returnBookById(BOOK_ID2)).isEqualTo(BOOK2_RETURN_SUCCESS_RESPONSE);
    }

    @Test
    @DisplayName("WHEN user tries to borrow a book that is not borrowed SHOULD return successful response")
    void whenUserBorrowsValidBookShouldReturnSuccess() {
        when(bookRepository.findById(BOOK_ID1)).thenReturn(Optional.of(BOOK1));
        when(authService.getCurrentUser()).thenReturn(USER1);
        assertThat(bookService.borrowBookById(BOOK_ID1)).isEqualTo(BOOK1_BORROW_SUCCESS_RESPONSE);
    }

    @Test
    @DisplayName("WHEN user tries to return a book that is borrowed by someone else SHOULD throw BookHasDifferentBorrowerException")
    void whenUserReturnsBorrowedBySomeoneElseBookShouldThrowException() {
        when(bookRepository.findById(BOOK_ID2)).thenReturn(Optional.of(BOOK2));
        when(authService.getCurrentUser()).thenReturn(USER2);
        assertThatThrownBy(() -> bookService.returnBookById(BOOK_ID2)).isInstanceOf(BookHasDifferentBorrowerException.class);
    }

    @Test
    @DisplayName("WHEN user tries to return a book that is not borrowed SHOULD throw BookNotBorrowedException")
    void whenUserReturnsNotBorrowedBookShouldThrowException() {
        when(bookRepository.findById(BOOK_ID1)).thenReturn(Optional.of(BOOK1));
        when(authService.getCurrentUser()).thenReturn(USER1);
        assertThatThrownBy(() -> bookService.returnBookById(BOOK_ID1)).isInstanceOf(BookNotBorrowedException.class);
    }

    @Test
    @DisplayName("WHEN user tries to borrow a book that is already borrowed SHOULD throw BoolAlreadyBorrowedException")
    void whenUserBorrowsBorrowedBookShouldThrowException() {
        when(bookRepository.findById(BOOK_ID2)).thenReturn(Optional.of(BOOK2));
        when(authService.getCurrentUser()).thenReturn(USER2);
        assertThatThrownBy(() -> bookService.borrowBookById(BOOK_ID2)).isInstanceOf(BookAlreadyBorrowedException.class);
    }
}
