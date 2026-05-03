package com.ytu.gymbackend.service;

import com.ytu.gymbackend.dto.ApiResponse;
import com.ytu.gymbackend.dto.request.MachineCreateRequest;
import com.ytu.gymbackend.exception.BadRequestException;
import com.ytu.gymbackend.model.machine.Machine;
import com.ytu.gymbackend.repository.MachineRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;


@Service
public class MachineServiceImpl implements MachineService{
    private final MachineRepository machineRepository;

    public MachineServiceImpl(MachineRepository machineRepository) {
        this.machineRepository = machineRepository;
    }

    @Override
    public ApiResponse createMachine(MachineCreateRequest request) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        LocalDate lastMaintenanceDate;
        try {
            lastMaintenanceDate = LocalDate.parse(request.getLastMaintenanceDate(), formatter);
        } catch (DateTimeParseException e) {
            throw new BadRequestException("wrong_date_format");
        }

        Machine machine = new Machine();
        machine.setName(request.getName());
        machine.setLastMaintenanceDate(lastMaintenanceDate);
        machineRepository.save(machine);


       return new ApiResponse(true, "machine_creation_successful");
    }
}
