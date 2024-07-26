package com.musiienko.library.model;


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
public class UpdateResponse {
    @Schema(description = "Update status description", example = "Book was deleted successfully")
    String status;
    @Schema(description = "Book id, related to the request", example = "124")
    @JsonProperty("book_id")
    Long id;
}
