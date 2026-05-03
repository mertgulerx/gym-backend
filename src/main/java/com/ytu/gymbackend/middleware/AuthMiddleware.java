package com.ytu.gymbackend.middleware;

import com.ytu.gymbackend.service.session.UserSessionService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.WebUtils;

import java.io.IOException;
import java.util.ArrayList;

@Component
public class AuthMiddleware extends OncePerRequestFilter {
    private final UserSessionService userSessionService;

    public AuthMiddleware(UserSessionService userSessionService) {
        this.userSessionService = userSessionService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        boolean authenticated = false;

        // Check User Session
        if (checkToken(request, "USER_SESSION", false)) {
            authenticated = true;
        }

        filterChain.doFilter(request, response);
    }

    private boolean checkToken(HttpServletRequest request, String name, boolean isClub) {
        Cookie sessionCookie = WebUtils.getCookie(request, name);

        if (sessionCookie != null) {
            String token = sessionCookie.getValue();
            boolean isValid = userSessionService.isSessionValid(token);

            if (isValid) {
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(token, null, new ArrayList<>());
                SecurityContextHolder.getContext().setAuthentication(authentication);
                return true;
            }
        }
        return false;
    }

}