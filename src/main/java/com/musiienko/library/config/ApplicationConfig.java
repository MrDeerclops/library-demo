package com.musiienko.library.config;

import com.musiienko.library.constant.Constant;
import com.musiienko.library.entity.Book;
import com.musiienko.library.entity.Category;
import com.musiienko.library.entity.User;
import com.musiienko.library.filter.JwtAuthFilter;
import com.musiienko.library.model.BookUpdateRequest;
import com.musiienko.library.model.BookViewResponse;
import com.musiienko.library.service.UserService;
import com.musiienko.library.util.LibraryUtil;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.List;
import java.util.Objects;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class ApplicationConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private final UserService userService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(request -> {
                    CorsConfiguration corsConfiguration = new CorsConfiguration();
                    corsConfiguration.setAllowedOriginPatterns(List.of("*"));
                    corsConfiguration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
                    corsConfiguration.setAllowedHeaders(List.of("*"));
                    corsConfiguration.setAllowCredentials(true);
                    return corsConfiguration;
                }))
                .authorizeHttpRequests(request -> request
                        .requestMatchers(HttpMethod.POST, "/api/books/new").hasRole(Constant.LIBRARIAN)
                        .requestMatchers(HttpMethod.PUT, "/api/books/{id}").hasRole(Constant.LIBRARIAN)
                        .requestMatchers(HttpMethod.DELETE, "/api/books/{id}").hasRole(Constant.LIBRARIAN)
                        .requestMatchers(HttpMethod.PUT, "/api/books/{id}/status").hasRole(Constant.USER)
                        .anyRequest().permitAll())
                .sessionManagement(manager -> manager.sessionCreationPolicy(STATELESS))
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config)
            throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        modelMapper.createTypeMap(Book.class, BookViewResponse.class)
                .addMappings(mapper -> mapper
                        .using((Converter<Category, String>) context -> context.getSource().label)
                        .map(Book::getCategory, BookViewResponse::setCategory))
                .addMappings(mapper -> mapper
                        .using((Converter<User, Boolean>) context -> Objects.nonNull(context.getSource()))
                        .map(Book::getUser, BookViewResponse::setBorrowed));

        modelMapper.createTypeMap(BookUpdateRequest.class, Book.class)
                .addMappings(mapper -> mapper
                        .using((Converter<String, Category>) context -> LibraryUtil.categoryFromString(context.getSource()).orElseThrow())
                        .map(BookUpdateRequest::getCategory, Book::setCategory));

        return modelMapper;
    }
}
