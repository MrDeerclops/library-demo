package com.musiienko.library;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "Library API", version = "1.0", description = "Library catalog"))
@SecurityScheme(name = "ROLE_USER", description = "Requires default user authorization", type = SecuritySchemeType.HTTP, scheme = "Bearer", bearerFormat = "JWT")
@SecurityScheme(name = "ROLE_LIBRARIAN", description = "Requires librarian authorization", type = SecuritySchemeType.HTTP, scheme = "Bearer", bearerFormat = "JWT")
public class LibraryApplication {

	public static void main(String[] args) {
		SpringApplication.run(LibraryApplication.class, args);
	}

}
