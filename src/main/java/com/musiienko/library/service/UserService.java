package com.musiienko.library.service;

import com.musiienko.library.entity.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    User getByUsername(String username);
    User save(User user);


}
