package com.ytu.gymbackend.controller;

import com.ytu.gymbackend.dto.ApiResponse;
import com.ytu.gymbackend.dto.request.LoginRequest;
import com.ytu.gymbackend.dto.response.UserResponse;
import com.ytu.gymbackend.exception.UnauthorizedException;
import com.ytu.gymbackend.model.user.User;
import com.ytu.gymbackend.model.user.UserSession;
import com.ytu.gymbackend.service.UserService;
import com.ytu.gymbackend.service.session.UserSessionService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final UserService userService;
    private final UserSessionService userSessionService;

    public AuthController(UserService userService, UserSessionService userSessionService) {
        this.userService = userService;
        this.userSessionService = userSessionService;
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(
            @Valid @RequestBody LoginRequest request,
            HttpServletResponse response
    ) {
        String token = userService.login(request);
        response.addCookie(createSessionCookie("USER_SESSION", token));
        return ResponseEntity.ok(new ApiResponse(true, "login_success"));
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse> logout(HttpServletResponse response) {
        userSessionService.logout();
        response.addCookie(removeSessionCookie("USER_SESSION"));
        return ResponseEntity.ok(new ApiResponse(true, "logout_success"));
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponse> me() {
        UserSession session = userSessionService.getCurrentSession()
                .orElseThrow(() -> new UnauthorizedException("unauthenticated"));
        User user = session.getUser();

        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setName(user.getName());
        response.setSurName(user.getSurName());
        response.setUserRole(user.getUserRole().toString());
        response.setAccountCreationDate(
                user.getAccountCreationDate() != null
                        ? user.getAccountCreationDate().toString()
                        : null
        );
        return ResponseEntity.ok(response);
    }

    private Cookie createSessionCookie(String name, String token) {
        Cookie cookie = new Cookie(name, token);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(24 * 60 * 60);
        return cookie;
    }

    private Cookie removeSessionCookie(String name){
        Cookie cookie = new Cookie(name, null);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        return cookie;
    }
}
