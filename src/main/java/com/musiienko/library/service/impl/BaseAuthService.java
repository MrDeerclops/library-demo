package com.musiienko.library.service.impl;

import com.musiienko.library.entity.User;
import com.musiienko.library.model.AuthRequest;
import com.musiienko.library.model.AuthResponse;
import com.musiienko.library.service.AuthService;
import com.musiienko.library.service.JwtService;
import com.musiienko.library.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BaseAuthService implements AuthService {

    UserService userService;
    JwtService jwtService;
    AuthenticationManager authenticationManager;

    @Override
    public AuthResponse authenticate(AuthRequest authRequest) {
        log.info("AUTH request for {}", authRequest.getUsername());
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                        authRequest.getUsername(),
                        authRequest.getPassword()
                )
        );
        UserDetails user = userService.loadUserByUsername(authRequest.getUsername());
        log.info("AUTH request for {} - SUCCESS, ID: {}", user.getUsername(), ((User) user).getId());
        String jwt = jwtService.generateToken(user);
        return new AuthResponse(jwt);
    }

    @Override
    public User getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userService.getByUsername(username);
    }
}
