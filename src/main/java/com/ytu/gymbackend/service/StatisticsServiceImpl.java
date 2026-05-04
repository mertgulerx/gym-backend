package com.ytu.gymbackend.service;

import com.ytu.gymbackend.dto.response.StatisticsResponse;
import com.ytu.gymbackend.model.machine.Maintenance;
import com.ytu.gymbackend.model.machine.Repair;
import com.ytu.gymbackend.model.subscription.SubscriptionPurchase;
import com.ytu.gymbackend.repository.MaintenanceRepository;
import com.ytu.gymbackend.repository.RepairRepository;
import com.ytu.gymbackend.repository.SubscriptionPurchaseRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static com.ytu.gymbackend.util.MapperUtil.formatter;

@Service
public class StatisticsServiceImpl implements StatisticsService{
    private final SubscriptionPurchaseRepository subscriptionPurchaseRepository;
    private final MaintenanceRepository maintenanceRepository;
    private final RepairRepository repairRepository;

    public StatisticsServiceImpl(SubscriptionPurchaseRepository subscriptionPurchaseRepository, MaintenanceRepository maintenanceRepository, RepairRepository repairRepository) {
        this.subscriptionPurchaseRepository = subscriptionPurchaseRepository;
        this.maintenanceRepository = maintenanceRepository;
        this.repairRepository = repairRepository;
    }

    @Override
    public StatisticsResponse getStatistics(String startDateString, String endDateString) {
        LocalDate startDate = LocalDate.parse(startDateString, formatter);
        LocalDate endDate = LocalDate.parse(endDateString, formatter);

        List<SubscriptionPurchase> subscriptionPurchaseList = subscriptionPurchaseRepository.findAllByCreationDateAfterAndCreationDateBefore(startDate, endDate);
        BigDecimal totalRevenue = new BigDecimal(0);
        BigDecimal maintenanceCost = new BigDecimal(0);
        BigDecimal repairCost = new BigDecimal(0);

        for (SubscriptionPurchase subscriptionPurchase : subscriptionPurchaseList){
            totalRevenue = totalRevenue.add(subscriptionPurchase.getTotalCost());
        }

        List<Maintenance> maintenanceList = maintenanceRepository.findAllByCreationDateAfterAndCreationDateBefore(startDate, endDate);
        for (Maintenance maintenance : maintenanceList){
            maintenanceCost = maintenanceCost.add(maintenance.getCost());
        }

        List<Repair> repairList = repairRepository.findAllBySentDateAfterAndSentDateBefore(startDate, endDate);
        for (Repair repair : repairList){
            repairCost = repairCost.add(repair.getCost());
        }

        StatisticsResponse statisticsResponse = new StatisticsResponse();
        statisticsResponse.setStartDate(startDateString);
        statisticsResponse.setEndDate(endDateString);
        statisticsResponse.setTotalRevenue(totalRevenue);
        statisticsResponse.setMaintenanceCosts(maintenanceCost);
        statisticsResponse.setRepairCosts(repairCost);

        return statisticsResponse;
    }
}
