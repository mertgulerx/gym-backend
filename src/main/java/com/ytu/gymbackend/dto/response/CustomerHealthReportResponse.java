package com.ytu.gymbackend.dto.response;

import lombok.Data;


@Data
public class CustomerHealthReportResponse {
    private Long id;

    private Long customerId;

    private String customerHealthReportStatus;

    private String revisionDate;

    private String endDate;

    private String fileName;
}
