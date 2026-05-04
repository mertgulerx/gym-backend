package com.ytu.gymbackend.controller;

import com.ytu.gymbackend.dto.response.StatisticsResponse;
import com.ytu.gymbackend.dto.response.SubscriptionPurchaseResponse;
import com.ytu.gymbackend.model.user.UserRole;
import com.ytu.gymbackend.service.StatisticsService;
import com.ytu.gymbackend.service.session.UserSessionService;
import com.ytu.gymbackend.validation.ValidDate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/statistics")
public class StatisticsController {
    private final UserSessionService userSessionService;
    private final StatisticsService statisticsService;

    public StatisticsController(UserSessionService userSessionService, StatisticsService statisticsService) {
        this.userSessionService = userSessionService;
        this.statisticsService = statisticsService;
    }

    @GetMapping("")
    public ResponseEntity<StatisticsResponse> getStatistics(
            @RequestParam(name = "startDate") @ValidDate String startDate,
            @RequestParam(name = "endDate") @ValidDate String endDate
    ) {
        userSessionService.validatePermission(new ArrayList<>(List.of(UserRole.ADMIN)));

        StatisticsResponse response = statisticsService.getStatistics(startDate, endDate);
        return ResponseEntity.status(200).body(response);
    }
}
