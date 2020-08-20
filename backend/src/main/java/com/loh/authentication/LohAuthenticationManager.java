package com.loh.authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCrypt;

public class LohAuthenticationManager implements AuthenticationManager {
    @Autowired
    UserRepository userRepository;
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        User user = userRepository.findByEmail(authentication.getName());
        String password = authentication.getCredentials().toString();
        if (BCrypt.checkpw(user.getPassword(), password)) {
            return new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword());
        } else {
            return null;
        }
    }
}
