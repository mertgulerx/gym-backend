package com.ytu.gymbackend.service;

import com.ytu.gymbackend.dto.request.LoginRequest;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    String login(@Valid LoginRequest request);
}
