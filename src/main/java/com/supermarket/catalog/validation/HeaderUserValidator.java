package com.supermarket.catalog.validation;

import com.supermarket.catalog.domain.user.User;
import com.supermarket.catalog.exception.UnauthorizedException;
import com.supermarket.catalog.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class HeaderUserValidator implements HandlerInterceptor {

    private static final String USERNAME_HEADER = "X-Username";
    private static final String USER_ID_HEADER = "X-User-Id";
    private static final String STORE_HEADER = "X-Store-Name";

    private final UserRepository userRepository;

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws UnauthorizedException {

        String username  = request.getHeader(USERNAME_HEADER);
        String userIdRaw = request.getHeader(USER_ID_HEADER);
        String storeName = request.getHeader(STORE_HEADER);

        Optional.ofNullable(username)
                .filter(u -> userIdRaw != null && storeName != null)
                .orElseThrow(() -> new UnauthorizedException("Missing authorization headers"));

        User user = Optional.of(userIdRaw)
                .map(UUID::fromString)
                .flatMap(userRepository::findById)
                .filter(u -> u.getUsername().equals(username))
                .filter(u -> u.getStore().getName().equals(storeName))
                .orElseThrow(() -> new UnauthorizedException("Unauthorized"));

        request.setAttribute("authenticatedUser", user);
        return true;
    }
}