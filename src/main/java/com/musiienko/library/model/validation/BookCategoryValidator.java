package com.musiienko.library.model.validation;

import com.musiienko.library.util.LibraryUtil;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class BookCategoryValidator implements ConstraintValidator<BookCategoryConstraint, String> {

    @Override
    public boolean isValid(String categoryField,
                           ConstraintValidatorContext cxt) {
        return LibraryUtil.categoryFromString(categoryField).isPresent();
    }

}
