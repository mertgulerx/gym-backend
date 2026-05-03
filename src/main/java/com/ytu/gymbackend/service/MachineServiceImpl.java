package com.ytu.gymbackend.service;

import com.ytu.gymbackend.dto.ApiResponse;
import com.ytu.gymbackend.dto.request.MachineCreateRequest;
import com.ytu.gymbackend.exception.BadRequestException;
import com.ytu.gymbackend.model.machine.Machine;
import com.ytu.gymbackend.repository.MachineRepository;
import com.ytu.gymbackend.util.MapperUtil;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;


@Service
public class MachineServiceImpl implements MachineService{
    private final MachineRepository machineRepository;
    private final MapperUtil mapperUtil;

    public MachineServiceImpl(MachineRepository machineRepository, MapperUtil mapperUtil) {
        this.machineRepository = machineRepository;
        this.mapperUtil = mapperUtil;
    }

    @Override
    public ApiResponse createMachine(MachineCreateRequest request) {
        Machine machine = mapperUtil.map(request, Machine.class);

        LocalDate lastMaintenanceDate = LocalDate.parse(request.getLastMaintenanceDate());
        machine.setLastMaintenanceDate(lastMaintenanceDate);
        machineRepository.save(machine);

       return new ApiResponse(true, "machine_creation_successful");
    }
}
