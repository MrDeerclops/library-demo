package com.musiienko.library.exception;

import com.musiienko.library.constant.Constant;

public class BookAlreadyBorrowedException extends LibraryException {
    public BookAlreadyBorrowedException(Long bookId) {
        super(Constant.BOOK_ALREADY_BORROWED_MESSAGE, bookId);
    }
}
