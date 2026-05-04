package com.ytu.gymbackend.service;

import com.ytu.gymbackend.dto.request.MaintenanceCreateRequest;
import com.ytu.gymbackend.dto.response.MaintenanceResponse;
import com.ytu.gymbackend.exception.NotFoundException;
import com.ytu.gymbackend.model.machine.Machine;
import com.ytu.gymbackend.model.machine.Maintenance;
import com.ytu.gymbackend.repository.MachineRepository;
import com.ytu.gymbackend.repository.MaintenanceRepository;
import com.ytu.gymbackend.service.session.UserSessionService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class MaintenanceServiceImpl implements MaintenanceService{
    private final MachineRepository machineRepository;
    private final UserSessionService userSessionService;
    private final MaintenanceRepository maintenanceRepository;

    public MaintenanceServiceImpl(MachineRepository machineRepository, UserSessionService userSessionService, MaintenanceRepository maintenanceRepository) {
        this.machineRepository = machineRepository;
        this.userSessionService = userSessionService;
        this.maintenanceRepository = maintenanceRepository;
    }

    @Override
    public MaintenanceResponse createMaintenance(Long machineId, MaintenanceCreateRequest request) {
        Machine machine = machineRepository.findById(machineId).orElseThrow(() -> new NotFoundException("machine_not_found"));

        Maintenance maintenance = new Maintenance();
        maintenance.setCost(request.getCost());
        maintenance.setInfo(request.getInfo());
        maintenance.setMachine(machine);
        maintenance.setMaintainer(userSessionService.getCurrentSession().get().getUser());
        maintenance = maintenanceRepository.save(maintenance);

        machine.setLastMaintenanceDate(maintenance.getCreationDate());
        machineRepository.save(machine);

        MaintenanceResponse maintenanceResponse = new MaintenanceResponse();
        maintenanceResponse.setId(maintenance.getId());
        maintenanceResponse.setMachineId(machineId);
        maintenanceResponse.setMaintainerId(maintenance.getMaintainer().getId());
        maintenanceResponse.setCost(maintenance.getCost());
        maintenanceResponse.setInfo(maintenance.getInfo());
        maintenanceResponse.setCreationDate(maintenance.getCreationDate().toString());
        return maintenanceResponse;
    }

    @Override
    public MaintenanceResponse getLastMaintenance(Long machineId) {
        Machine machine = machineRepository.findById(machineId).orElseThrow(() -> new NotFoundException("machine_not_found"));

        List<Maintenance> maintenanceList = machine.getMaintenanceList();

        if (maintenanceList.isEmpty()){
            throw new NotFoundException("maintenance_not_found");
        }

        maintenanceList.sort(Comparator.comparing(Maintenance::getCreationDate).reversed());
        Maintenance maintenance = maintenanceList.getFirst();


        MaintenanceResponse maintenanceResponse = new MaintenanceResponse();
        maintenanceResponse.setId(maintenance.getId());
        maintenanceResponse.setMachineId(machineId);
        maintenanceResponse.setMaintainerId(maintenance.getMaintainer().getId());
        maintenanceResponse.setCost(maintenance.getCost());
        maintenanceResponse.setInfo(maintenance.getInfo());
        maintenanceResponse.setCreationDate(maintenance.getCreationDate().toString());
        return maintenanceResponse;
    }

    @Override
    public List<MaintenanceResponse> getAllMaintenances(Long machineId) {
        Machine machine = machineRepository.findById(machineId).orElseThrow(() -> new NotFoundException("machine_not_found"));

        List<Maintenance> maintenanceList = machine.getMaintenanceList();

        if (maintenanceList.isEmpty()){
            throw new NotFoundException("maintenance_not_found");
        }

        maintenanceList.sort(Comparator.comparing(Maintenance::getCreationDate).reversed());

        List<MaintenanceResponse> maintenanceResponseList = new ArrayList<>();

        for (Maintenance maintenance : maintenanceList){

            MaintenanceResponse maintenanceResponse = new MaintenanceResponse();
            maintenanceResponse.setId(maintenance.getId());
            maintenanceResponse.setMachineId(machineId);
            maintenanceResponse.setMaintainerId(maintenance.getMaintainer().getId());
            maintenanceResponse.setCost(maintenance.getCost());
            maintenanceResponse.setInfo(maintenance.getInfo());
            maintenanceResponse.setCreationDate(maintenance.getCreationDate().toString());
            maintenanceResponseList.add(maintenanceResponse);
        }

        return maintenanceResponseList;
    }


}
