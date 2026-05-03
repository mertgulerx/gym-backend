package com.ytu.gymbackend.service.session;

import com.ytu.gymbackend.model.user.User;
import com.ytu.gymbackend.model.user.UserSession;
import com.ytu.gymbackend.model.user.UserType;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface UserSessionService {
    void validatePermission(UserType userType);

    UserSession createSession(User user, int hoursValid);
    boolean isSessionValid(String token);
    void invalidateSession(String token);

    void logout();

    Optional<UserSession> getActiveSession(User user);
    Optional<UserSession> getCurrentSession();
}
