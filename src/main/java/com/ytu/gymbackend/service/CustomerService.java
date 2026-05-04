package com.ytu.gymbackend.service;

import com.ytu.gymbackend.dto.ApiResponse;
import com.ytu.gymbackend.dto.request.CustomerRegisterRequest;
import com.ytu.gymbackend.dto.response.CustomerRegisterResponse;
import com.ytu.gymbackend.dto.response.CustomerResponse;
import com.ytu.gymbackend.model.customer.CustomerHealthReport;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

@Service
public interface CustomerService {
    CustomerRegisterResponse register(@Valid CustomerRegisterRequest request);

    ApiResponse uploadHealthReport(Long id, MultipartFile file);

    CustomerHealthReport getHealthReport(Long id);

    ApiResponse verifyHealthReport(Long id, LocalDate revisionDate);

    CustomerResponse getCustomer(@NotNull Long id);

    List<CustomerResponse> getAllCustomers();

    CustomerResponse updateCustomer(@NotNull Long id);
}
