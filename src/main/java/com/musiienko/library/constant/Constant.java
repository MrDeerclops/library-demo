package com.musiienko.library.constant;

public class Constant {
    public static final String ROLE_CLAIM = "role";
    public static final int JWT_EXPIRATION_TIME_MILLIS = 1000 * 60 * 60 * 24 * 7; // = 7 days
    public static final String JWT_SIGNING_KEY = "GOT A SECRET, CAN YOU KEEP IT";
    public static final String BEARER_PREFIX = "Bearer ";
    public static final String AUTH_HEADER_NAME = "Authorization";

    public static final String LIBRARIAN = "LIBRARIAN";
    public static final String USER = "USER";
    public static final String BOOK_CREATED_MESSAGE = "Book was created successfully";
    public static final String BOOK_UPDATED_MESSAGE = "Book was updated successfully";
    public static final String BOOK_DELETED_MESSAGE = "Book was deleted successfully";
    public static final String BOOK_BORROWED_MESSAGE = "Book was borrowed successfully";
    public static final String BOOK_RETURNED_MESSAGE = "Book was returned successfully";

    public static final String BOOK_NOT_FOUND_MESSAGE = "Book with provided id does not exist";
    public static final String BOOK_ALREADY_BORROWED_MESSAGE = "Book with provided id is already borrowed";
    public static final String BOOK_NOT_BORROWED_MESSAGE = "Book with provided id is not borrowed to return";
    public static final String BOOK_DIFFERENT_BORROWER_MESSAGE = "Book with provided id is borrowed by another user";

}
