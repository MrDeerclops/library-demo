package com.musiienko.library.util;

import com.musiienko.library.entity.Category;
import lombok.experimental.UtilityClass;

import java.util.Optional;
import java.util.StringJoiner;

@UtilityClass
public class LibraryUtil {
    public Optional<Category> categoryFromString(String field) {
        for (Category category : Category.values()) {
            if (category.label.equalsIgnoreCase(field)) {
                return Optional.of(category);
            }
        }
        return Optional.empty();
    }

    public String categoriesListing() {
        StringJoiner joiner = new StringJoiner(", ");
        for (Category category : Category.values()) {
            joiner.add(category.label);
        }
        return joiner.toString();
    }
}
