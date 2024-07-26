package com.musiienko.library.model;

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
@Schema(description = "Auth request for JWT-token")
public class AuthRequest {
    @Schema(description = "Username", example = "john_forester")
    @NotBlank(message = "Username can not be empty")
    @Size(min = 4, max = 20, message = "Username must be from 4 to 20 characters long")
    @Pattern(regexp = "[a-zA-Z][a-zA-Z\\d_-]+", message = "Username must start with latin letter " +
            "and can contain only latin letters, digits and '-' and '_' characters")
    String username;

    @Schema(description = "Password", example = "s0me_str0ng_p@ssw0rd")
    @NotBlank(message = "Password can not be empty")
    @Size(min = 10, max = 40, message = "Password must be from 10 to 40 characters long")
    String password;
}
