package com.musiienko.library.exception;

import com.musiienko.library.constant.Constant;

public class BookNotFoundException extends LibraryException {
    public BookNotFoundException(Long bookId) {
        super(Constant.BOOK_NOT_FOUND_MESSAGE, bookId);
    }
}
