package com.musiienko.library.model.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = BookCategoryValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface BookCategoryConstraint {
    String message() default "Category must be one of the list: ";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
