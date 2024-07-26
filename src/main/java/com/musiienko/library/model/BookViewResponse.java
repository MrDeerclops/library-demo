package com.musiienko.library.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Representation of a book in the library catalog")
public class BookViewResponse {
    @Schema(description = "Book identifier, that can be used for requests", example = "123")
    Long id;
    @Schema(description = "ISBN", example = "9781848940871")
    String ISBN;
    @Schema(description = "Book title", example = "A Dance with Dragons")
    String title;
    @Schema(description = "Book author", example = "Stephen King")
    String author;
    @Schema(description = "Book category", example = "Fantasy")
    String category;
    @Schema(description = "Whether the book is borrowed by some user", example = "true")
    boolean borrowed;
}
