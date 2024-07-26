package com.musiienko.library.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.musiienko.library.model.validation.BookCategoryConstraint;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request to add new book to the library catalog")
public class BookUpdateRequest {
    @Schema(description = "ISBN", example = "9781848940871")
    @NotBlank(message = "ISBN can not be empty")
    @Size(min = 13, max = 13, message = "ISBN must be 13 characters long")
    @Pattern(regexp = "\\d+", message = "ISBN must contain only digits")
    @JsonProperty("ISBN")
    String ISBN;
    @Schema(description = "Book title", example = "A Dance with Dragons")
    @NotBlank(message = "Book title can not be empty")
    @Size(max = 255, message = "Book title can not be more than 255 characters long")
    String title;
    @Schema(description = "Book author", example = "Stephen King")
    @NotBlank(message = "Book author can not be empty")
    @Size(max = 255, message = "Book author can not be more than 255 characters long")
    String author;
    @Schema(description = "Book category", example = "Fantasy")
    @NotBlank(message = "Book category can not be empty")
    @BookCategoryConstraint
    String category;
}
