package com.ytu.gymbackend.service;

import com.ytu.gymbackend.dto.ApiResponse;
import com.ytu.gymbackend.dto.request.CustomerRegisterRequest;
import com.ytu.gymbackend.model.customer.CustomerHealthReport;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Service
public interface CustomerService {
    ApiResponse register(@Valid CustomerRegisterRequest request);

    ApiResponse uploadHealthReport(String tcKimlikNo, MultipartFile file);

    CustomerHealthReport getHealthReport(String tcKimlikNo);

    ApiResponse verifyHealthReport(String tcKimlikNo, LocalDate revisionDate);
}
