
package com.bruno.reservo.controllers;

import com.bruno.reservo.dto.BusinessPublicDTO;
import com.bruno.reservo.dto.ServicePublicDTO;
import com.bruno.reservo.entities.Business;
import com.bruno.reservo.entities.ServiceEntity;
import com.bruno.reservo.repositories.BusinessRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/public/business")
@RequiredArgsConstructor
@CrossOrigin("*")
public class BusinessPublicController {

    private final BusinessRepository businessRepository;

    @GetMapping("/{businessId}")
    public BusinessPublicDTO getBusiness(@PathVariable Long businessId) {
        Business b = businessRepository.findById(businessId)
                .orElseThrow(() -> new RuntimeException("Business not found"));

        return new BusinessPublicDTO(
                b.getId(),
                b.getName(),
                b.getAddress(),
                b.getPhone()
        );
    }

    @GetMapping("/slug/{slug}")
    public BusinessPublicDTO getBusinessBySlug(@PathVariable String slug) {
        Business b = businessRepository.findBySlug(slug)
                .orElseThrow(() -> new RuntimeException("Business not found"));

        return new BusinessPublicDTO(
                b.getId(),
                b.getName(),
                b.getAddress(),
                b.getPhone()
        );
    }


    @GetMapping("/{businessId}/services")
    public List<ServicePublicDTO> getBusinessServices(@PathVariable Long businessId) {

        Business business = businessRepository.findById(businessId)
                .orElseThrow(() -> new RuntimeException("Business not found"));

        List<ServiceEntity> services = business.getServices();

        return services.stream()
                .map(s -> new ServicePublicDTO(
                        s.getId(),
                        s.getName(),
                        s.getPrice(),
                        s.getDurationMinutes()
                ))
                .toList();
    }
}
