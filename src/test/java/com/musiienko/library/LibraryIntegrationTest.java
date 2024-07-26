package com.musiienko.library;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.musiienko.library.constant.Constant;
import com.musiienko.library.model.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class LibraryIntegrationTest {

    private static final String USERNAME_USER1 = "user1";
    private static final String USERNAME_LIBRARIAN = "admin";
    private static final String PASSWORD_USER1 = "strong_user_password1";
    private static final String PASSWORD_USER2 = "strong_user_password2";
    private static final String PASSWORD_LIBRARIAN = "strong_admin_password";

    private static final String NEW_BOOK_ISBN = "9780809590478";
    private static final String NEW_BOOK_TITLE = "Harry Potter and the Goblet of Fire";
    private static final String NEW_BOOK_AUTHOR = "J.K. Rowling";
    private static final String NEW_BOOK_CATEGORY = "Fantasy";
    private static final String BAD_CATEGORY = "K-Pop";

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("As a LIBRARIAN I sign-in, add new book and fetch it by id. SHOULD fetch added book")
    void signInAddNewBookAndFetchIt() throws Exception {
        MvcResult authResult = mockMvc.perform(post("/api/auth")
                .content(objectMapper.writeValueAsBytes(AuthRequest.builder()
                        .username(USERNAME_LIBRARIAN)
                        .password(PASSWORD_LIBRARIAN).build()
                )).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andReturn();

        String jwt = objectMapper.readValue(authResult.getResponse().getContentAsString(), AuthResponse.class).getToken();

        MvcResult updateResult = mockMvc.perform(post("/api/books/new")
                        .content(objectMapper.writeValueAsBytes(
                                BookUpdateRequest.builder()
                                        .ISBN(NEW_BOOK_ISBN)
                                        .title(NEW_BOOK_TITLE)
                                        .author(NEW_BOOK_AUTHOR)
                                        .category(NEW_BOOK_CATEGORY)
                                        .build()
                        ))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(Constant.AUTH_HEADER_NAME, Constant.BEARER_PREFIX + jwt))
                .andExpect(status().isOk()).andReturn();
        Long bookId = objectMapper.readValue(updateResult.getResponse().getContentAsString(), UpdateResponse.class).getId();
        MvcResult fetchBookResult = mockMvc.perform(get("/api/books/{id}", bookId))
                .andExpect(status().isOk())
                .andReturn();
        BookViewResponse actualResult = objectMapper.readValue(fetchBookResult.getResponse().getContentAsString(), BookViewResponse.class);
        assertThat(actualResult).extracting(BookViewResponse::getId).isEqualTo(bookId);
        assertThat(actualResult).extracting(BookViewResponse::getISBN).isEqualTo(NEW_BOOK_ISBN);
        assertThat(actualResult).extracting(BookViewResponse::getTitle).isEqualTo(NEW_BOOK_TITLE);
        assertThat(actualResult).extracting(BookViewResponse::getAuthor).isEqualTo(NEW_BOOK_AUTHOR);
        assertThat(actualResult).extracting(BookViewResponse::getCategory).isEqualTo(NEW_BOOK_CATEGORY);
    }

    @Test
    @DisplayName("As a USER I sign-in and try edit book. SHOULD receive status FORBIDDEN")
    void signInAndTryEditBook() throws Exception {
        MvcResult authResult = mockMvc.perform(post("/api/auth")
                .content(objectMapper.writeValueAsBytes(AuthRequest.builder()
                        .username(USERNAME_USER1)
                        .password(PASSWORD_USER1).build()
                )).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andReturn();

        String jwt = objectMapper.readValue(authResult.getResponse().getContentAsString(), AuthResponse.class).getToken();

        mockMvc.perform(put("/api/books/{id}", 1L)
                        .content(objectMapper.writeValueAsBytes(
                                BookUpdateRequest.builder()
                                        .ISBN(NEW_BOOK_ISBN)
                                        .title(NEW_BOOK_TITLE)
                                        .author(NEW_BOOK_AUTHOR)
                                        .category(NEW_BOOK_CATEGORY)
                                        .build()
                        ))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(Constant.AUTH_HEADER_NAME, Constant.BEARER_PREFIX + jwt))
                .andExpect(status().isForbidden()).andReturn();
    }

    @Test
    @DisplayName("As a LIBRARIAN I sign-in and try create new book with not valid category. SHOULD receive status BAD REQUEST")
    void signInAndTryCreateBookWithBadCategory() throws Exception {
        MvcResult authResult = mockMvc.perform(post("/api/auth")
                .content(objectMapper.writeValueAsBytes(AuthRequest.builder()
                        .username(USERNAME_LIBRARIAN)
                        .password(PASSWORD_LIBRARIAN).build()
                )).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andReturn();

        String jwt = objectMapper.readValue(authResult.getResponse().getContentAsString(), AuthResponse.class).getToken();

        mockMvc.perform(post("/api/books/new")
                        .content(objectMapper.writeValueAsBytes(
                                BookUpdateRequest.builder()
                                        .ISBN(NEW_BOOK_ISBN)
                                        .title(NEW_BOOK_TITLE)
                                        .author(NEW_BOOK_AUTHOR)
                                        .category(BAD_CATEGORY)
                                        .build()
                        ))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(Constant.AUTH_HEADER_NAME, Constant.BEARER_PREFIX + jwt))
                .andExpect(status().isBadRequest()).andReturn();
    }

    @Test
    @DisplayName("As a USER I with wrong password. SHOULD receive status FORBIDDEN")
    void signInWithWrongPassword() throws Exception {
        mockMvc.perform(post("/api/auth")
                .content(objectMapper.writeValueAsBytes(AuthRequest.builder()
                        .username(USERNAME_USER1)
                        .password(PASSWORD_USER2).build()
                )).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isForbidden()).andReturn();

    }

    @Test
    @DisplayName("As a USER I sign-in and try borrow unoccupied book, then fetch it, then try borrow it again. SHOULD receive status BAD REQUEST")
    void signInAndTryBorrowBookAndFetchItAndBorrowItAgain() throws Exception {
        MvcResult authResult = mockMvc.perform(post("/api/auth")
                .content(objectMapper.writeValueAsBytes(AuthRequest.builder()
                        .username(USERNAME_USER1)
                        .password(PASSWORD_USER1).build()
                )).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andReturn();

        String jwt = objectMapper.readValue(authResult.getResponse().getContentAsString(), AuthResponse.class).getToken();

        MvcResult updateResult = mockMvc.perform(put("/api/books/{id}/borrow", 1L)
                        .header(Constant.AUTH_HEADER_NAME, Constant.BEARER_PREFIX + jwt))
                .andExpect(status().isOk()).andReturn();
        Long bookId = objectMapper.readValue(updateResult.getResponse().getContentAsString(), UpdateResponse.class).getId();

        MvcResult fetchBookResult = mockMvc.perform(get("/api/books/{id}", bookId))
                .andExpect(status().isOk())
                .andReturn();
        BookViewResponse actualResult = objectMapper.readValue(fetchBookResult.getResponse().getContentAsString(), BookViewResponse.class);
        assertThat(actualResult).extracting(BookViewResponse::isBorrowed).isEqualTo(true);

        mockMvc.perform(put("/api/books/{id}/borrow", 1L)
                        .header(Constant.AUTH_HEADER_NAME, Constant.BEARER_PREFIX + jwt))
                .andExpect(status().isBadRequest()).andReturn();
    }
}
