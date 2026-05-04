package com.ytu.gymbackend.service;

import com.ytu.gymbackend.dto.response.StatisticsResponse;
import org.springframework.stereotype.Service;

@Service
public interface StatisticsService {
    StatisticsResponse getStatistics(String startDateString, String endDateString);
}
