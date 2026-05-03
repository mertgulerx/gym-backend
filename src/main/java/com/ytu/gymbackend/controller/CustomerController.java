package com.ytu.gymbackend.controller;

import com.ytu.gymbackend.dto.ApiResponse;
import com.ytu.gymbackend.dto.request.CustomerRegisterRequest;
import com.ytu.gymbackend.dto.request.UserRegisterRequest;
import com.ytu.gymbackend.model.customer.CustomerHealthReport;
import com.ytu.gymbackend.model.user.UserType;
import com.ytu.gymbackend.service.CustomerService;
import com.ytu.gymbackend.service.session.UserSessionService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/customer")
public class CustomerController {
    private final UserSessionService userSessionService;
    private final CustomerService customerService;

    public CustomerController(@Qualifier("userSessionService") UserSessionService userSessionService, @Qualifier("customerService") CustomerService customerService) {
        this.userSessionService = userSessionService;
        this.customerService = customerService;
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse> registerCustomer(
            @Valid @RequestBody CustomerRegisterRequest request
    ) {
        userSessionService.validatePermission(new ArrayList<>(List.of(UserType.ADMIN, UserType.CLERK)));
        ApiResponse response = customerService.register(request);
        return ResponseEntity.status(response.isSuccess() ? 200 : 400).body(response);
    }

    @PostMapping("/health_report/upload")
    public ResponseEntity<ApiResponse> uploadHealthReport(
            @RequestParam(name = "tcKimlikNo") @NotNull @NotBlank String tcKimlikNo,
            @RequestParam("file") @NotNull MultipartFile file
    ) {
        userSessionService.validatePermission(new ArrayList<>(List.of(UserType.ADMIN, UserType.CLERK)));

        ApiResponse response = customerService.uploadHealthReport(tcKimlikNo, file);

        if (file.isEmpty() || !Objects.equals(file.getContentType(), "application/pdf")) {
            return ResponseEntity.status(400).body(new ApiResponse(false, "wrong_file_format"));
        }

        return ResponseEntity.status(response.isSuccess() ? 200 : 400).body(response);
    }

    @GetMapping("/health_report/download")
    public ResponseEntity<ByteArrayResource> downloadReport(@RequestParam(name = "tcKimlikNo") @NotNull @NotBlank String tcKimlikNo) {
        CustomerHealthReport customerHealthReport = customerService.getHealthReport(tcKimlikNo);

        ByteArrayResource resource = new ByteArrayResource(customerHealthReport.getPdfData());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + customerHealthReport.getFileName() + ".pdf\"")
                .contentType(MediaType.APPLICATION_PDF)
                .contentLength(customerHealthReport.getPdfData().length)
                .body(resource);
    }





}
