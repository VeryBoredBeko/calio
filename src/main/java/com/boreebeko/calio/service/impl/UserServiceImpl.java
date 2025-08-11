package com.boreebeko.calio.service.impl;

import com.boreebeko.calio.service.UserService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    @Override
    public UUID getCurrentUserUUID() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof Jwt jwt)) {
            throw new IllegalStateException("User is not authenticated or principal is not a Jwt");
        }
        String userIdStr = jwt.getClaimAsString("sub");
        if (userIdStr == null || userIdStr.isEmpty()) {
            throw new IllegalStateException("JWT does not contain 'sub' claim");
        }
        try {
            return UUID.fromString(userIdStr);
        } catch (IllegalArgumentException e) {
            throw new IllegalStateException("Invalid UUID format in 'sub' claim", e);
        }
    }

}
