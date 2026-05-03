package com.ytu.gymbackend.service;

import com.ytu.gymbackend.dto.ApiResponse;
import com.ytu.gymbackend.dto.request.MachineCreateRequest;
import com.ytu.gymbackend.exception.BadRequestException;
import com.ytu.gymbackend.model.customer.Customer;
import com.ytu.gymbackend.model.customer.CustomerHealthReport;
import com.ytu.gymbackend.model.machine.Machine;
import com.ytu.gymbackend.repository.MachineRepository;
import com.ytu.gymbackend.util.MapperUtil;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.Optional;


@Service
public class MachineServiceImpl implements MachineService{
    private final MachineRepository machineRepository;
    private final MapperUtil mapperUtil;

    public MachineServiceImpl(MachineRepository machineRepository, MapperUtil mapperUtil) {
        this.machineRepository = machineRepository;
        this.mapperUtil = mapperUtil;
    }

    @Override
    public ApiResponse createMachine(MachineCreateRequest request, @NotNull MultipartFile image) {
        Machine machine = mapperUtil.map(request, Machine.class);

        LocalDate lastMaintenanceDate = LocalDate.parse(request.getLastMaintenanceDate());
        machine.setLastMaintenanceDate(lastMaintenanceDate);

        try {
            machine.setImage(image.getBytes());
            machineRepository.save(machine);
        } catch (Exception e) {
            throw new BadRequestException("failed_to_save_file");
        }

        return new ApiResponse(true, "machine_creation_successful");
    }

    @Override
    public byte[] getImage(Long id) {
        Machine machine = machineRepository.findById(id).orElseThrow(() -> new BadRequestException("machine_not_found"));

        if (machine.getImage() == null){
            throw new BadRequestException("image_not_found");
        }

        return machine.getImage();
    }
}
