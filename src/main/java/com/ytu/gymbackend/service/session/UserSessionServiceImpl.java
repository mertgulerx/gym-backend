package com.ytu.gymbackend.service.session;

import com.ytu.gymbackend.exception.UnauthorizedException;
import com.ytu.gymbackend.model.user.User;
import com.ytu.gymbackend.model.user.UserSession;
import com.ytu.gymbackend.model.user.UserType;
import com.ytu.gymbackend.repository.UserSessionRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class UserSessionServiceImpl implements UserSessionService {

    private final UserSessionRepository userSessionRepository;

    public UserSessionServiceImpl(UserSessionRepository userSessionRepository) {
        this.userSessionRepository = userSessionRepository;
    }

    @Override
    public void validatePermission(UserType userType){
        Optional<UserSession> optionalUserSession = getCurrentSession();
        if (optionalUserSession.isEmpty()){
            throw new UnauthorizedException("unauthorized");
        }

        UserSession userSession = optionalUserSession.get();
        if (!userSession.getUser().getUserType().equals(userType)){
            throw new UnauthorizedException("unauthorized");
        }
    }

    @Override
    public void validatePermission(List<UserType> userTypeList){
        Optional<UserSession> optionalUserSession = getCurrentSession();
        if (optionalUserSession.isEmpty()){
            throw new UnauthorizedException("unauthorized");
        }

        UserSession userSession = optionalUserSession.get();
        boolean authorized = false;

        for (UserType userType : userTypeList){
            if (userSession.getUser().getUserType().equals(userType)) {
                authorized = true;
                break;
            }
        }
        if (!authorized){
            throw new UnauthorizedException("unauthorized");
        }
    }

    @Override
    public UserSession createSession(User user, int hoursValid) {
        UserSession session = UserSession.createSession(user, hoursValid);
        return userSessionRepository.save(session);
    }

    @Override
    public boolean isSessionValid(String token) {
        Optional<UserSession> sessionOpt = userSessionRepository.findByTokenAndActiveTrue(token);
        if (sessionOpt.isEmpty()) return false;

        UserSession session = sessionOpt.get();
        if (session.getExpiresAt().isBefore(LocalDateTime.now())) {
            session.setActive(false);
            userSessionRepository.save(session);
            return false;
        }
        return true;
    }

    @Override
    public void invalidateSession(String token) {
        userSessionRepository.findByTokenAndActiveTrue(token).ifPresent(session -> {
            session.setActive(false);
            userSessionRepository.save(session);
        });
    }

    @Override
    public void logout() {
        getCurrentSession().ifPresent(session -> {
            session.setActive(false);
            userSessionRepository.save(session);
        });
    }

    @Override
    public Optional<UserSession> getActiveSession(User user) {
        return userSessionRepository.findAll().stream()
                .filter(s -> s.getUser().equals(user) && s.isActive())
                .findFirst();
    }

    @Override
    public Optional<UserSession> getCurrentSession() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return Optional.empty();
        }
        // Assuming the principal is the token string as handled in AuthMiddleware
        Object principal = authentication.getPrincipal();
        if (!(principal instanceof String token)) {
            return Optional.empty();
        }
        return userSessionRepository.findByTokenAndActiveTrue(token);
    }
}
