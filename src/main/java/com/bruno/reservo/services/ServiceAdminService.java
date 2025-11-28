package com.bruno.reservo.services;

import com.bruno.reservo.dto.ServiceAdminDTO;
import com.bruno.reservo.entities.Business;
import com.bruno.reservo.entities.ServiceEntity;
import com.bruno.reservo.repositories.BusinessRepository;
import com.bruno.reservo.repositories.ServiceRepository;
import com.bruno.reservo.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ServiceAdminService {

    private final ServiceRepository serviceRepo;
    private final BusinessRepository businessRepo;
    private final UserService userService;

    public List<ServiceAdminDTO> getByBusiness(Long businessId) {
        return serviceRepo.findByBusinessId(businessId)
                .stream()
                .map(s -> {
                    ServiceAdminDTO dto = new ServiceAdminDTO();
                    dto.setId(s.getId());
                    dto.setName(s.getName());
                    dto.setDurationMinutes(s.getDurationMinutes());
                    dto.setPrice(s.getPrice());
                    return dto;
                })
                .toList();
    }

    public ServiceEntity create(Long businessId, ServiceEntity req) {

        Business business = businessRepo.findById(businessId)
                .orElseThrow(() -> new RuntimeException("Business not found"));

        if (!business.getOwner().getId().equals(userService.getLoggedUserId()))
            throw new RuntimeException("No autorizado");

        ServiceEntity s = ServiceEntity.builder()
                .name(req.getName())
                .durationMinutes(req.getDurationMinutes())
                .price(req.getPrice())
                .business(business)
                .build();

        return serviceRepo.save(s);
    }

    public void delete(Long id) {
        serviceRepo.deleteById(id);
    }
}
