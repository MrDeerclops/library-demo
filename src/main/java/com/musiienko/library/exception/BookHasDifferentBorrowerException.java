package com.musiienko.library.exception;

import com.musiienko.library.constant.Constant;

public class BookHasDifferentBorrowerException extends LibraryException {
    public BookHasDifferentBorrowerException(Long bookId) {
        super(Constant.BOOK_DIFFERENT_BORROWER_MESSAGE, bookId);
    }
}
