package com.bruno.reservo.controllers;

import com.bruno.reservo.entities.Business;
import com.bruno.reservo.entities.ServiceEntity;
import com.bruno.reservo.services.BusinessService;
import com.bruno.reservo.repositories.ServiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/services")
@RequiredArgsConstructor
public class ServiceAdminController {

    private final ServiceRepository serviceRepo;
    private final BusinessService businessService;

    @GetMapping("/{businessId}")
    public List<ServiceEntity> getByBusiness(@PathVariable Long businessId) {
        businessService.validateOwnership(businessId);
        Business b = businessService.getMyBusinessById(businessId);
        return serviceRepo.findByBusinessId(b.getId());
    }

    @PostMapping("/{businessId}")
    public ServiceEntity create(
            @PathVariable Long businessId,
            @RequestBody ServiceEntity data) {
        businessService.validateOwnership(businessId);

        Business b = businessService.getMyBusinessById(businessId);

        ServiceEntity s = ServiceEntity.builder()
                .name(data.getName())
                .price(data.getPrice())
                .durationMinutes(data.getDurationMinutes())
                .business(b)
                .build();

        return serviceRepo.save(s);
    }

    @DeleteMapping("/{serviceId}")
    public void delete(@PathVariable Long serviceId) {
        businessService.validateOwnershipByService(serviceId);
        serviceRepo.deleteById(serviceId);
    }
}
