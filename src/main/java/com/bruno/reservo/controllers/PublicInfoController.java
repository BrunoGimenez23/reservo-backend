package com.bruno.reservo.controllers;

import com.bruno.reservo.entities.Business;
import com.bruno.reservo.entities.ServiceEntity;
import com.bruno.reservo.repositories.BusinessRepository;
import com.bruno.reservo.repositories.ServiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/public/info")
@RequiredArgsConstructor
public class PublicInfoController {

    private final BusinessRepository businessRepo;
    private final ServiceRepository serviceRepo;

    @GetMapping("/{businessId}/{serviceId}")
    public Map<String, Object> info(
            @PathVariable Long businessId,
            @PathVariable Long serviceId
    ) {
        Business b = businessRepo.findById(businessId)
                .orElseThrow(() -> new RuntimeException("Business not found"));

        ServiceEntity s = serviceRepo.findById(serviceId)
                .orElseThrow(() -> new RuntimeException("Service not found"));

        return Map.of(
                "business", b,
                "service", s
        );
    }
}
