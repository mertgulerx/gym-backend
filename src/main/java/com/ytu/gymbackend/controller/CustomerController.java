package com.ytu.gymbackend.controller;

import com.ytu.gymbackend.dto.ApiResponse;
import com.ytu.gymbackend.dto.request.CustomerRegisterRequest;
import com.ytu.gymbackend.dto.request.UserRegisterRequest;
import com.ytu.gymbackend.model.customer.CustomerHealthReport;
import com.ytu.gymbackend.model.user.UserType;
import com.ytu.gymbackend.service.CustomerService;
import com.ytu.gymbackend.service.session.UserSessionService;
import com.ytu.gymbackend.validation.ValidDate;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.lang.reflect.Array;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/customer")
public class CustomerController {
    private final UserSessionService userSessionService;
    private final CustomerService customerService;

    public CustomerController( UserSessionService userSessionService, CustomerService customerService) {
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
            @RequestParam(name = "id") @NotNull Long id,
            @RequestParam("file") @NotNull MultipartFile file
    ) {
        userSessionService.validatePermission(new ArrayList<>(List.of(UserType.ADMIN, UserType.CLERK)));

        if (file.isEmpty() || !Objects.equals(file.getContentType(), "application/pdf")) {
            return ResponseEntity.status(400).body(new ApiResponse(false, "wrong_file_format"));
        }

        ApiResponse response = customerService.uploadHealthReport(id, file);

        return ResponseEntity.status(response.isSuccess() ? 200 : 400).body(response);
    }

    @GetMapping("/health_report/get")
    public ResponseEntity<ByteArrayResource> getHealthReport(@RequestParam(name = "id") @NotNull Long id) {
        userSessionService.validatePermission(new ArrayList<>(List.of(UserType.ADMIN, UserType.CLERK)));

        CustomerHealthReport customerHealthReport = customerService.getHealthReport(id);

        ByteArrayResource resource = new ByteArrayResource(customerHealthReport.getPdfData());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + customerHealthReport.getFileName() + ".pdf\"")
                .contentType(MediaType.APPLICATION_PDF)
                .contentLength(customerHealthReport.getPdfData().length)
                .body(resource);
    }


    @PutMapping("/health_report/verify")
    public ResponseEntity<ApiResponse> verifyHealthReport(
            @RequestParam(name = "id") @NotNull Long id,
            @RequestParam(name = "revisionDate") @NotBlank @ValidDate String revisionDate
    ) {
        userSessionService.validatePermission(new ArrayList<>(List.of(UserType.ADMIN, UserType.CLERK)));

        ApiResponse response = customerService.verifyHealthReport(id, LocalDate.parse(revisionDate));

        return ResponseEntity.status(response.isSuccess() ? 200 : 400).body(response);

    }

}
