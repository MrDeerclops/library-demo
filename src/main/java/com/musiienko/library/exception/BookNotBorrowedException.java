package com.musiienko.library.exception;

import com.musiienko.library.constant.Constant;

public class BookNotBorrowedException extends LibraryException {
    public BookNotBorrowedException(Long bookId) {
        super(Constant.BOOK_NOT_BORROWED_MESSAGE, bookId);
    }
}
