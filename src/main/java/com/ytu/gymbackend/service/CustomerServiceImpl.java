package com.ytu.gymbackend.service;

import com.ytu.gymbackend.dto.ApiResponse;
import com.ytu.gymbackend.dto.request.CustomerRegisterRequest;
import com.ytu.gymbackend.dto.response.CustomerRegisterResponse;
import com.ytu.gymbackend.dto.response.CustomerResponse;
import com.ytu.gymbackend.dto.response.UserResponse;
import com.ytu.gymbackend.exception.BadRequestException;
import com.ytu.gymbackend.exception.NotFoundException;
import com.ytu.gymbackend.model.customer.Customer;
import com.ytu.gymbackend.model.customer.CustomerHealthReport;
import com.ytu.gymbackend.model.customer.CustomerHealthReportStatus;
import com.ytu.gymbackend.model.customer.CustomerStatus;
import com.ytu.gymbackend.model.subscription.Subscription;
import com.ytu.gymbackend.model.subscription.SubscriptionStatus;
import com.ytu.gymbackend.model.user.User;
import com.ytu.gymbackend.repository.CustomerHealthReportRepository;
import com.ytu.gymbackend.repository.CustomerRepository;
import com.ytu.gymbackend.util.MapperUtil;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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
    public CustomerRegisterResponse register(CustomerRegisterRequest request) {
        customerRepository.findByPhoneNumber(request.getPhoneNumber()).ifPresent(customer -> {
            throw new BadRequestException("customer_already_exist");
        });

        Customer customer = mapperUtil.map(request, Customer.class);
        customer.setCustomerStatus(CustomerStatus.PENDING);
        customer = customerRepository.save(customer);
        return mapperUtil.map(customer, CustomerRegisterResponse.class);
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
            customerHealthReport.setFileName(customer.getName() + "_" + customer.getSurName() + "_health-report_" + LocalDateTime.now());
            customerHealthReportRepository.save(customerHealthReport);
        } catch (Exception e) {
            throw new BadRequestException("failed_to_save_file");
        }

        return new ApiResponse(true, "customer_health_report_created");
    }

    @Override
    public CustomerHealthReport getHealthReport(Long id) {
        Customer customer = findCustomerById(id);

        CustomerHealthReport customerHealthReport = customer.getCustomerHealthReport();
        if (customerHealthReport == null){
            throw new NotFoundException("health_report_not_found");
        }
        return customerHealthReport;
    }

    @Override
    public ApiResponse verifyHealthReport(Long id, LocalDate revisionDate) {
        Customer customer = findCustomerById(id);

        CustomerHealthReport customerHealthReport = customer.getCustomerHealthReport();
        if (customerHealthReport == null){
            throw new NotFoundException("health_report_not_found");
        }

        customerHealthReport.setCustomerHealthReportStatus(CustomerHealthReportStatus.VERIFIED);
        customerHealthReport.setRevisionDate(revisionDate);
        customerHealthReport.setEndDate(revisionDate.plusYears(1));
        customerHealthReportRepository.save(customerHealthReport);
        return new ApiResponse(true, "customer_health_report_verified");
    }

    @Override
    public CustomerResponse getCustomer(Long id) {
        Customer customer = findCustomerById(id);

        CustomerResponse customerResponse = mapperUtil.map(customer, CustomerResponse.class);

        customerResponse.setCustomerStatus(customer.getCustomerStatus().toString());

        Subscription subscription = customer.getSubscription();
        customerResponse.setIsActiveSubscriber(false);

        if (subscription != null && subscription.getStatus().equals(SubscriptionStatus.PAID)){
            customerResponse.setIsActiveSubscriber(true);
        }
        customerResponse.setAccountCreationDate(customer.getAccountCreationDate().toString());

        return customerResponse;
    }

    @Override
    public List<CustomerResponse> getAllCustomers() {
        List<Customer> allCustomers = customerRepository.findAll();

        if (allCustomers.isEmpty()){
            throw new NotFoundException("customer_not_found");
        }

        List<CustomerResponse> customerResponseList = new ArrayList<>();

        for (Customer customer : allCustomers){
            CustomerResponse customerResponse = mapperUtil.map(customer, CustomerResponse.class);

            customerResponse.setCustomerStatus(customer.getCustomerStatus().toString());

            Subscription subscription = customer.getSubscription();
            customerResponse.setIsActiveSubscriber(false);

            if (subscription != null && subscription.getStatus().equals(SubscriptionStatus.PAID)){
                customerResponse.setIsActiveSubscriber(true);
            }
            customerResponse.setAccountCreationDate(customer.getAccountCreationDate().toString());
            customerResponseList.add(customerResponse);
        }

        return customerResponseList;
    }

    @Override
    public CustomerResponse updateCustomer(Long id, CustomerRegisterRequest request) {
        Customer customer = findCustomerById(id);
        customer.setName(request.getName());
        customer.setSurName(request.getSurName());
        customer.setPhoneNumber(request.getPhoneNumber());
        customer = customerRepository.save(customer);

        CustomerResponse customerResponse = mapperUtil.map(customer, CustomerResponse.class);

        customerResponse.setCustomerStatus(customer.getCustomerStatus().toString());

        Subscription subscription = customer.getSubscription();
        customerResponse.setIsActiveSubscriber(false);

        if (subscription != null && subscription.getStatus().equals(SubscriptionStatus.PAID)){
            customerResponse.setIsActiveSubscriber(true);
        }
        customerResponse.setAccountCreationDate(customer.getAccountCreationDate().toString());

        return customerResponse;
    }

    private Customer findCustomerById(Long id){
        return customerRepository.findById(id).orElseThrow(() -> new NotFoundException("customer_not_found"));
    }
}
