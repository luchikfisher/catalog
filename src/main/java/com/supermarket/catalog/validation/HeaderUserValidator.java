package com.supermarket.catalog.validation;

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
                             Object handler) {

        String username = request.getHeader(USERNAME_HEADER);
        String userId = request.getHeader(USER_ID_HEADER);

        if (Objects.isNull(username) || Objects.isNull(userId)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }

        UUID id;
        try {
            id = UUID.fromString(userId);
        } catch (IllegalArgumentException e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }

        boolean valid = userRepository.findById(id)
                .map(u -> u.getUsername().equals(username))
                .orElse(false);

        if (!valid) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }

        return valid;
    }
}