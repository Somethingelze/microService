package com.some.micro.configuration;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class SecurityProvider {

    public String getUsername() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

}
