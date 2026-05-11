package com.ytu.gymbackend.service;

import com.ytu.gymbackend.dto.ApiResponse;
import com.ytu.gymbackend.dto.request.MachineCreateRequest;
import com.ytu.gymbackend.dto.response.MachineResponse;
import com.ytu.gymbackend.dto.response.UserResponse;
import com.ytu.gymbackend.exception.BadRequestException;
import com.ytu.gymbackend.exception.NotFoundException;
import com.ytu.gymbackend.model.customer.Customer;
import com.ytu.gymbackend.model.customer.CustomerHealthReport;
import com.ytu.gymbackend.model.machine.Machine;
import com.ytu.gymbackend.model.machine.MachineStatus;
import com.ytu.gymbackend.model.user.User;
import com.ytu.gymbackend.repository.MachineRepository;
import com.ytu.gymbackend.util.MapperUtil;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.ytu.gymbackend.util.MapperUtil.formatter;


@Service
public class MachineServiceImpl implements MachineService{
    private final MachineRepository machineRepository;
    private final MapperUtil mapperUtil;

    public MachineServiceImpl(MachineRepository machineRepository, MapperUtil mapperUtil) {
        this.machineRepository = machineRepository;
        this.mapperUtil = mapperUtil;
    }

    @Override
    public MachineResponse createMachine(MachineCreateRequest request, @NotNull MultipartFile image) {
        Machine machine = mapperUtil.map(request, Machine.class);

        LocalDate lastMaintenanceDate = LocalDate.parse(request.getLastMaintenanceDate(), formatter);
        machine.setLastMaintenanceDate(lastMaintenanceDate);
        machine.setMachineStatus(MachineStatus.AVAILABLE);

        try {
            machine.setImage(image.getBytes());
            machine = machineRepository.save(machine);
        } catch (Exception e) {
            throw new BadRequestException("failed_to_save_file");
        }

        return mapperUtil.map(machine, MachineResponse.class);
    }

    @Override
    public byte[] getImage(Long id) {
        Machine machine = machineRepository.findById(id).orElseThrow(() -> new NotFoundException("machine_not_found"));

        if (machine.getImage() == null){
            throw new NotFoundException("image_not_found");
        }

        return machine.getImage();
    }

    @Override
    public MachineResponse getMachine(Long id) {
        Machine machine = machineRepository.findById(id).orElseThrow(() -> new NotFoundException("machine_not_found"));

        MachineResponse machineResponse = mapperUtil.map(machine, MachineResponse.class);

        machineResponse.setMachineStatus(machine.getMachineStatus().toString());

        return machineResponse;
    }

    @Override
    public List<MachineResponse> getAllMachines() {
        List<Machine> allMachines = machineRepository.findAll();

        if (allMachines.isEmpty()){
            throw new NotFoundException("machine_not_found");
        }

        List<MachineResponse> machineResponseList = new ArrayList<>();

        for (Machine machine : allMachines){
            MachineResponse machineResponse = mapperUtil.map(machine, MachineResponse.class);
            machineResponse.setMachineStatus(machine.getMachineStatus().toString());
            machineResponseList.add(machineResponse);
        }

        return machineResponseList;
    }
}
