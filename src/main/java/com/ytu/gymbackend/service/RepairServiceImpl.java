package com.ytu.gymbackend.service;

import com.ytu.gymbackend.dto.ApiResponse;
import com.ytu.gymbackend.dto.request.RepairCreateRequest;
import com.ytu.gymbackend.dto.response.RepairResponse;
import com.ytu.gymbackend.exception.BadRequestException;
import com.ytu.gymbackend.exception.NotFoundException;
import com.ytu.gymbackend.model.machine.Machine;
import com.ytu.gymbackend.model.machine.MachineStatus;
import com.ytu.gymbackend.model.machine.Repair;
import com.ytu.gymbackend.repository.MachineRepository;
import com.ytu.gymbackend.repository.RepairRepository;
import com.ytu.gymbackend.service.session.UserSessionService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class RepairServiceImpl implements RepairService{
    private final MachineRepository machineRepository;
    private final UserSessionService userSessionService;
    private final RepairRepository repairRepository;

    public RepairServiceImpl(MachineRepository machineRepository, UserSessionService userSessionService, RepairRepository repairRepository) {
        this.machineRepository = machineRepository;
        this.userSessionService = userSessionService;
        this.repairRepository = repairRepository;
    }

    @Override
    public RepairResponse createRepair(Long machineId, RepairCreateRequest request) {
        Machine machine = machineRepository.findById(machineId).orElseThrow(() -> new NotFoundException("machine_not_found"));

        if (!machine.getMachineStatus().equals(MachineStatus.AVAILABLE)){
            throw new BadRequestException("machine_is_not_available");
        }

        Repair repair = new Repair();
        repair.setCost(request.getCost());
        repair.setInfo(request.getInfo());
        repair.setMachine(machine);
        repair.setMaintainer(userSessionService.getCurrentSession().get().getUser());
        repair.setEstimatedReturnDays(request.getEstimatedReturnDays());
        repair.setIsCompleted(request.getIsCompleted());
        repair = repairRepository.save(repair);

        machine.setMachineStatus(MachineStatus.ON_REPAIR_SERVICE);
        machineRepository.save(machine);

        RepairResponse repairResponse = new RepairResponse();
        repairResponse.setId(repair.getId());
        repairResponse.setMachineId(machineId);
        repairResponse.setMaintainerId(repair.getMaintainer().getId());
        repairResponse.setCost(repair.getCost());
        repairResponse.setInfo(repair.getInfo());
        repairResponse.setSentDate(repair.getSentDate().toString());
        repairResponse.setIsCompleted(repair.getIsCompleted());
        repairResponse.setEstimatedReturnDays(repair.getEstimatedReturnDays());
        return repairResponse;
    }

    @Override
    public ApiResponse completeRepair(Long machineId) {
        Machine machine = machineRepository.findById(machineId).orElseThrow(() -> new NotFoundException("machine_not_found"));

        if (!machine.getMachineStatus().equals(MachineStatus.ON_REPAIR_SERVICE)){
            throw new BadRequestException("machine_is_not_on_repair_service");
        }

        List<Repair> repairList = machine.getRepairList();
        repairList.sort(Comparator.comparing(Repair::getSentDate).reversed());
        Repair repair = repairList.getFirst();

        repair.setIsCompleted(true);
        repair.setCompleteDate(LocalDate.now());
        repairRepository.save(repair);
        machine.setMachineStatus(MachineStatus.AVAILABLE);
        machineRepository.save(machine);
        return new ApiResponse(true, "repair_completed_successfuly");
    }

    @Override
    public RepairResponse getLastRepair(Long machineId) {
        Machine machine = machineRepository.findById(machineId).orElseThrow(() -> new NotFoundException("machine_not_found"));

        List<Repair> repairList = machine.getRepairList();

        if (repairList.isEmpty()){
            throw new NotFoundException("repair_not_found");
        }

        repairList.sort(Comparator.comparing(Repair::getSentDate).reversed());
        Repair repair = repairList.getFirst();

        RepairResponse repairResponse = new RepairResponse();
        repairResponse.setId(repair.getId());
        repairResponse.setMachineId(machineId);
        repairResponse.setMaintainerId(repair.getMaintainer().getId());
        repairResponse.setCost(repair.getCost());
        repairResponse.setInfo(repair.getInfo());
        repairResponse.setSentDate(repair.getSentDate().toString());
        repairResponse.setIsCompleted(repair.getIsCompleted());
        if (repair.getCompleteDate() != null){
            repairResponse.setCompleteDay(repair.getCompleteDate().toString());
        }
        repairResponse.setEstimatedReturnDays(repair.getEstimatedReturnDays());
        return repairResponse;
    }

    @Override
    public List<RepairResponse> getAllRepairs(Long machineId) {
        Machine machine = machineRepository.findById(machineId).orElseThrow(() -> new NotFoundException("machine_not_found"));

        List<Repair> repairList = machine.getRepairList();

        if (repairList.isEmpty()){
            throw new NotFoundException("repair_not_found");
        }

        repairList.sort(Comparator.comparing(Repair::getSentDate).reversed());

        List<RepairResponse> repairResponseList = new ArrayList<>();

        for (Repair repair : repairList){

            RepairResponse repairResponse = new RepairResponse();
            repairResponse.setId(repair.getId());
            repairResponse.setMachineId(machineId);
            repairResponse.setMaintainerId(repair.getMaintainer().getId());
            repairResponse.setCost(repair.getCost());
            repairResponse.setInfo(repair.getInfo());
            repairResponse.setSentDate(repair.getSentDate().toString());
            repairResponse.setIsCompleted(repair.getIsCompleted());
            if (repair.getCompleteDate() != null){
                repairResponse.setCompleteDay(repair.getCompleteDate().toString());
            }
            repairResponse.setEstimatedReturnDays(repair.getEstimatedReturnDays());
            repairResponseList.add(repairResponse);
        }

        return repairResponseList;
    }
}
