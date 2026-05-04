package com.ytu.gymbackend.controller;

import com.ytu.gymbackend.dto.ApiResponse;
import com.ytu.gymbackend.dto.request.CustomerRegisterRequest;
import com.ytu.gymbackend.dto.response.CustomerHealthReportResponse;
import com.ytu.gymbackend.dto.response.CustomerRegisterResponse;
import com.ytu.gymbackend.dto.response.CustomerResponse;
import com.ytu.gymbackend.dto.response.UserResponse;
import com.ytu.gymbackend.model.customer.CustomerHealthReport;
import com.ytu.gymbackend.model.user.UserRole;
import com.ytu.gymbackend.service.CustomerService;
import com.ytu.gymbackend.service.session.UserSessionService;
import com.ytu.gymbackend.validation.ValidDate;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.ytu.gymbackend.util.MapperUtil.formatter;

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
    public ResponseEntity<CustomerRegisterResponse> registerCustomer(
            @Valid @RequestBody CustomerRegisterRequest request
    ) {
        userSessionService.validatePermission(new ArrayList<>(List.of(UserRole.ADMIN, UserRole.CLERK)));
        CustomerRegisterResponse response = customerService.register(request);
        return ResponseEntity.status(200).body(response);
    }

    @PostMapping("/{id}/health_report")
    public ResponseEntity<ApiResponse> uploadHealthReport(
            @PathVariable @NotNull Long id,
            @RequestParam("file") @NotNull MultipartFile file
    ) {
        userSessionService.validatePermission(new ArrayList<>(List.of(UserRole.ADMIN, UserRole.CLERK)));

        if (file.isEmpty() || !Objects.equals(file.getContentType(), "application/pdf")) {
            return ResponseEntity.status(400).body(new ApiResponse(false, "wrong_file_format"));
        }

        ApiResponse response = customerService.uploadHealthReport(id, file);

        return ResponseEntity.status(response.isSuccess() ? 200 : 400).body(response);
    }

    @GetMapping("/{id}/health_report/document")
    public ResponseEntity<ByteArrayResource> getHealthReportDocument(@PathVariable @NotNull Long id) {
        userSessionService.validatePermission(new ArrayList<>(List.of(UserRole.ADMIN, UserRole.CLERK)));

        CustomerHealthReport customerHealthReport = customerService.getHealthReport(id);

        ByteArrayResource resource = new ByteArrayResource(customerHealthReport.getPdfData());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + customerHealthReport.getFileName() + ".pdf\"")
                .contentType(MediaType.APPLICATION_PDF)
                .contentLength(customerHealthReport.getPdfData().length)
                .body(resource);
    }

    @GetMapping("/{id}/health_report")
    public ResponseEntity<CustomerHealthReportResponse> getHealthReport(@PathVariable @NotNull Long id) {
        userSessionService.validatePermission(new ArrayList<>(List.of(UserRole.ADMIN, UserRole.CLERK)));

        CustomerHealthReport customerHealthReport = customerService.getHealthReport(id);

        CustomerHealthReportResponse response = new CustomerHealthReportResponse();
        response.setCustomerId(customerHealthReport.getCustomer().getId());
        response.setId(customerHealthReport.getId());
        response.setRevisionDate(customerHealthReport.getRevisionDate().toString());
        response.setEndDate(customerHealthReport.getEndDate().toString());
        response.setCustomerHealthReportStatus(customerHealthReport.getCustomerHealthReportStatus().toString());
        response.setFileName(customerHealthReport.getFileName());

        return ResponseEntity.status(200).body(response);
    }


    @PutMapping("/{id}/health_report/verify")
    public ResponseEntity<ApiResponse> verifyHealthReport(
            @PathVariable @NotNull Long id,
            @RequestParam(name = "revisionDate") @NotBlank @ValidDate String revisionDate
    ) {
        userSessionService.validatePermission(new ArrayList<>(List.of(UserRole.ADMIN, UserRole.CLERK)));

        ApiResponse response = customerService.verifyHealthReport(id, LocalDate.parse(revisionDate, formatter));

        return ResponseEntity.status(response.isSuccess() ? 200 : 400).body(response);

    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponse> getUser(@PathVariable @NotNull Long id) {
        userSessionService.validatePermission(new ArrayList<>(List.of(UserRole.ADMIN, UserRole.CLERK)));
        CustomerResponse response = customerService.getCustomer(id);
        return ResponseEntity.status(200).body(response);
    }

    @GetMapping("")
    public ResponseEntity<List<CustomerResponse>> getAllUsers() {
        userSessionService.validatePermission(new ArrayList<>(List.of(UserRole.ADMIN, UserRole.CLERK)));
        List<CustomerResponse> response = customerService.getAllCustomers();
        return ResponseEntity.status(200).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerResponse> updateUser(@PathVariable @NotNull Long id,
                                                       @Valid @RequestBody CustomerRegisterRequest request) {
        userSessionService.validatePermission(new ArrayList<>(List.of(UserRole.ADMIN, UserRole.CLERK)));
        CustomerResponse response = customerService.updateCustomer(id, request);
        return ResponseEntity.status(200).body(response);
    }

}
