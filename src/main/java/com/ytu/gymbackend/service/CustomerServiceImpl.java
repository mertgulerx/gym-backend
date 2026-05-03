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
import com.ytu.gymbackend.util.EncryptUtil;
import com.ytu.gymbackend.util.MapperUtil;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class CustomerServiceImpl implements CustomerService{
    private final CustomerRepository customerRepository;
    private final EncryptUtil encryptUtil;
    private final MapperUtil mapperUtil;
    private final CustomerHealthReportRepository customerHealthReportRepository;

    public CustomerServiceImpl(CustomerRepository customerRepository, EncryptUtil encryptUtil, MapperUtil mapperUtil, CustomerHealthReportRepository customerHealthReportRepository) {
        this.customerRepository = customerRepository;
        this.encryptUtil = encryptUtil;
        this.mapperUtil = mapperUtil;
        this.customerHealthReportRepository = customerHealthReportRepository;
    }

    @Override
    public ApiResponse register(CustomerRegisterRequest request) {
        String tcKimlikNo = request.getTcKimlikNo();
        String tcKimlikNoIndex = encryptUtil.generateBlindIndex(tcKimlikNo);

        customerRepository.findByTcKimlikNoIndex(tcKimlikNoIndex).ifPresent(customer -> {
            throw new BadRequestException("customer_already_exist");
        });

        Customer customer = mapperUtil.map(request, Customer.class);
        customer.setCustomerStatus(CustomerStatus.PENDING);
        customer.setTcKimlikNoIndex(tcKimlikNoIndex);
        customer.setTcKimlikNoEncrypted(encryptUtil.encryptAES(tcKimlikNo));
        customerRepository.save(customer);
        return new ApiResponse(true, "customer_created");
    }

    private Customer findCustomerByTcKimlikNo(String tcKimlikNo){
        String tcKimlikNoIndex = encryptUtil.generateBlindIndex(tcKimlikNo);

        Optional<Customer> optionalCustomer = customerRepository.findByTcKimlikNoIndex(tcKimlikNoIndex);

        if (optionalCustomer.isPresent()){
            return optionalCustomer.get();
        }

        throw new BadRequestException("customer_not_found");
    }

    @Override
    public ApiResponse uploadHealthReport(String tcKimlikNo, MultipartFile file) {
        Customer customer = findCustomerByTcKimlikNo(tcKimlikNo);

        try {
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
    public CustomerHealthReport getHealthReport(String tcKimlikNo) {
        Customer customer = findCustomerByTcKimlikNo(tcKimlikNo);

        CustomerHealthReport customerHealthReport = customer.getCustomerHealthReport();
        if (customerHealthReport == null){
            throw new BadRequestException("health_report_not_found");
        }
        return  customerHealthReport;
    }


}
