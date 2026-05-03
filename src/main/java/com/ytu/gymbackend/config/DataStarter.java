package com.ytu.gymbackend.config;

import com.ytu.gymbackend.model.user.User;
import com.ytu.gymbackend.model.user.UserType;
import com.ytu.gymbackend.repository.UserRepository;
import com.ytu.gymbackend.util.PasswordUtils;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
public class DataStarter implements ApplicationListener<ContextRefreshedEvent> {
    private final PasswordUtils passwordUtils;
    private final UserRepository userRepository;

    public DataStarter(PasswordUtils passwordUtils, UserRepository userRepository) {
        this.passwordUtils = passwordUtils;
        this.userRepository = userRepository;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        User user = new User();
        user.setUserType(UserType.ADMIN);
        user.setHashedPassword(passwordUtils.hashPassword("password123ytu"));
        user.setBackupSecret(passwordUtils.hashPassword("ytu"));
        userRepository.save(user);
    }
}
