package com.ytu.gymbackend.service;

import com.ytu.gymbackend.dto.ApiResponse;
import com.ytu.gymbackend.dto.request.LoginRequest;
import com.ytu.gymbackend.dto.request.UserRegisterRequest;
import com.ytu.gymbackend.dto.response.UserRegisterResponse;
import com.ytu.gymbackend.dto.response.UserResponse;
import com.ytu.gymbackend.exception.BadRequestException;
import com.ytu.gymbackend.exception.NotFoundException;
import com.ytu.gymbackend.exception.UnauthorizedException;
import com.ytu.gymbackend.model.user.User;
import com.ytu.gymbackend.model.user.UserSession;
import com.ytu.gymbackend.model.user.UserRole;
import com.ytu.gymbackend.repository.UserRepository;
import com.ytu.gymbackend.service.session.UserSessionService;
import com.ytu.gymbackend.util.MapperUtil;
import com.ytu.gymbackend.util.PasswordUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordUtils passwordUtils;
    private final UserSessionService userSessionService;
    private final MapperUtil mapperUtil;

    public UserServiceImpl(UserRepository userRepository, PasswordUtils passwordUtils, UserSessionService userSessionService, MapperUtil mapperUtil, MapperUtil mapperUtil1) {
        this.userRepository = userRepository;
        this.passwordUtils = passwordUtils;
        this.userSessionService = userSessionService;
        this.mapperUtil = mapperUtil1;
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

    @Override
    public UserRegisterResponse register(UserRegisterRequest request) {
        User newUser = new User();
        newUser.setUserRole(UserRole.valueOf(request.getUserType()));
        newUser.setHashedPassword(passwordUtils.hashPassword(request.getPassword()));
        if (request.getBackupSecret() != null){
            newUser.setBackupSecret(passwordUtils.hashPassword(request.getBackupSecret()));
        }
        newUser.setName(request.getName());
        newUser.setSurName(request.getSurName());
        newUser =  userRepository.save(newUser);

        return mapperUtil.map(newUser, UserRegisterResponse.class);
    }

    @Override
    public ApiResponse passwordReset(Long id, String backupSecret, String newPassword) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UnauthorizedException("invalid_credentials"));

        String hashedBackupSecret = user.getBackupSecret();
        if (hashedBackupSecret == null){
            throw new BadRequestException("no_backup_secret");
        }

        if (!passwordUtils.checkPassword(backupSecret, hashedBackupSecret)){
            throw new UnauthorizedException("invalid_credentials");
        }

        user.setHashedPassword(passwordUtils.hashPassword(newPassword));
        userRepository.save(user);

        return new ApiResponse(true, "password_reset_success");
    }

    @Override
    public UserResponse getUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("user_not_found"));

        UserResponse userResponse = mapperUtil.map(user, UserResponse.class);

        userResponse.setUserRole(user.getUserRole().toString());
        userResponse.setAccountCreationDate(user.getAccountCreationDate().toString());
        return userResponse;
    }

    @Override
    public List<UserResponse> getAllUsers() {
        List<User> allUsers = userRepository.findAll();

        if (allUsers.isEmpty()){
            throw new NotFoundException("user_not_found");
        }

        List<UserResponse> userResponseList = new ArrayList<>();

        for (User user : allUsers){
            UserResponse userResponse = mapperUtil.map(user, UserResponse.class);
            userResponse.setUserRole(user.getUserRole().toString());
            userResponse.setAccountCreationDate(user.getAccountCreationDate().toString());
            userResponseList.add(userResponse);
        }

        return userResponseList;
    }
}
