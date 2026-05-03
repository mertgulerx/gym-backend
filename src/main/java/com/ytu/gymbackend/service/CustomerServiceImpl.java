package com.ytu.gymbackend.service;

import com.ytu.gymbackend.dto.ApiResponse;
import com.ytu.gymbackend.dto.request.CustomerRegisterRequest;
import com.ytu.gymbackend.exception.BadRequestException;
import com.ytu.gymbackend.model.customer.Customer;
import com.ytu.gymbackend.model.customer.CustomerHealthReport;
import com.ytu.gymbackend.model.customer.CustomerHealthReportStatus;
import com.ytu.gymbackend.model.customer.CustomerStatus;
import com.ytu.gymbackend.repository.CustomerHealthReportRepository;
import com.ytu.gymbackend.repository.CustomerRepository;
import com.ytu.gymbackend.util.MapperUtil;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class CustomerServiceImpl implements CustomerService{
    private final CustomerRepository customerRepository;
    private final MapperUtil mapperUtil;
    private final CustomerHealthReportRepository customerHealthReportRepository;

    public CustomerServiceImpl(CustomerRepository customerRepository, MapperUtil mapperUtil, CustomerHealthReportRepository customerHealthReportRepository) {
        this.customerRepository = customerRepository;
        this.mapperUtil = mapperUtil;
        this.customerHealthReportRepository = customerHealthReportRepository;
    }

    @Override
    public ApiResponse register(CustomerRegisterRequest request) {
        customerRepository.findByPhoneNumber(request.getPhoneNumber()).ifPresent(customer -> {
            throw new BadRequestException("customer_already_exist");
        });

        Customer customer = mapperUtil.map(request, Customer.class);
        customer.setCustomerStatus(CustomerStatus.PENDING);
        customerRepository.save(customer);
        return new ApiResponse(true, "customer_created");
    }

    @Override
    @Transactional
    public ApiResponse uploadHealthReport(Long id, MultipartFile file) {
        Customer customer = findCustomerById(id);

        try {
            CustomerHealthReport existingCustomerHealthReport = customer.getCustomerHealthReport();
            if (existingCustomerHealthReport != null){
                customerHealthReportRepository.delete(customer.getCustomerHealthReport());
            }

            CustomerHealthReport customerHealthReport = new CustomerHealthReport();

            customerHealthReport.setPdfData(file.getBytes());
            customerHealthReport.setCustomer(customer);
            customerHealthReport.setCustomerHealthReportStatus(CustomerHealthReportStatus.PENDING);
            customerHealthReport.setFileName(customer.getName() + "_" + customer.getSurName() + "_health-report_" + LocalDateTime.now().toString());
            customerHealthReportRepository.save(customerHealthReport);
        } catch (Exception e) {
            throw new BadRequestException("Failed to store PDF file" + e.getMessage());
        }

        return new ApiResponse(true, "customer_health_report_created");
    }

    @Override
    public CustomerHealthReport getHealthReport(Long id) {
        Customer customer = findCustomerById(id);

        CustomerHealthReport customerHealthReport = customer.getCustomerHealthReport();
        if (customerHealthReport == null){
            throw new BadRequestException("health_report_not_found");
        }
        return customerHealthReport;
    }

    @Override
    public ApiResponse verifyHealthReport(Long id, LocalDate revisionDate) {
        Customer customer = findCustomerById(id);

        CustomerHealthReport customerHealthReport = customer.getCustomerHealthReport();
        if (customerHealthReport == null){
            throw new BadRequestException("health_report_not_found");
        }

        customerHealthReport.setCustomerHealthReportStatus(CustomerHealthReportStatus.VERIFIED);
        customerHealthReport.setRevisionDate(revisionDate);
        customerHealthReport.setEndDate(revisionDate.plusYears(1));
        customerHealthReportRepository.save(customerHealthReport);
        return new ApiResponse(true, "customer_health_report_verified");
    }

    private Customer findCustomerById(Long id){
        Optional<Customer> optionalCustomer = customerRepository.findById(id);

        if (optionalCustomer.isPresent()){
            return optionalCustomer.get();
        }

        throw new BadRequestException("customer_not_found");
    }


}
