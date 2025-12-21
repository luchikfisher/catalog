package com.supermarket.catalog.validation;

import com.supermarket.catalog.exception.UnauthorizedException;
import com.supermarket.catalog.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Objects;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class HeaderUserValidator implements HandlerInterceptor {

    private static final String USERNAME_HEADER = "X-Username";
    private static final String USER_ID_HEADER = "X-User-Id";

    private final UserRepository userRepository;

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler)
            throws UnauthorizedException {

        String username = request.getHeader(USERNAME_HEADER);
        String userId = request.getHeader(USER_ID_HEADER);

        if (Objects.isNull(username) || Objects.isNull(userId)) {
            throw new UnauthorizedException("Missing authentication headers");
        }

        UUID id;
        try {
            id = UUID.fromString(userId);
        } catch (IllegalArgumentException e) {
            throw new UnauthorizedException("Invalid user ID format");
        }

        userRepository.findById(id)
                .filter(u -> u.getUsername().equals(username))
                .orElseThrow(() ->
                        new UnauthorizedException("Invalid username or user ID")
                );

        return true;
    }
}