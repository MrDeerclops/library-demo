package com.musiienko.library.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Error response, when something went wrong")
public class ErrorResponse {
    @Schema(description = "Error category", example = "Validation error")
    String error;
    @Schema(description = "Error detailed description", example = "ISBN must be 13 characters long")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    String details;
    @Schema(description = "Book id, related to the request", example = "124")
    @JsonProperty("book_id")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    Long id;
}
