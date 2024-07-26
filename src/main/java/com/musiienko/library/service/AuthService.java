package com.musiienko.library.service;

import com.musiienko.library.entity.User;
import com.musiienko.library.model.AuthRequest;
import com.musiienko.library.model.AuthResponse;

public interface AuthService {
    AuthResponse authenticate(AuthRequest authRequest);
    User getCurrentUser();
}
