package com.ytu.gymbackend.service;

import com.ytu.gymbackend.dto.request.LoginRequest;
import com.ytu.gymbackend.exception.UnauthorizedException;
import com.ytu.gymbackend.model.user.User;
import com.ytu.gymbackend.model.user.UserSession;
import com.ytu.gymbackend.repository.UserRepository;
import com.ytu.gymbackend.service.session.UserSessionService;
import com.ytu.gymbackend.utils.PasswordUtils;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordUtils passwordUtils;
    private final UserSessionService userSessionService;

    public UserServiceImpl(UserRepository userRepository, PasswordUtils passwordUtils, UserSessionService userSessionService) {
        this.userRepository = userRepository;
        this.passwordUtils = passwordUtils;
        this.userSessionService = userSessionService;
    }

    @Override
    public String login(LoginRequest loginRequest) {
        User user = userRepository.findById(loginRequest.getId())
                .orElseThrow(() -> new UnauthorizedException("invalid_credentials"));

        if (!passwordUtils.checkPassword(loginRequest.getPassword(), user.getHashedPassword())) {
            throw new UnauthorizedException("invalid_credentials");
        }

        UserSession session = userSessionService.createSession(user, 24);

        return session.getToken();
    }
}
