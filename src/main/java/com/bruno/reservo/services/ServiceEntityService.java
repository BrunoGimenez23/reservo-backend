package com.bruno.reservo.services;

import com.bruno.reservo.entities.Business;
import com.bruno.reservo.entities.ServiceEntity;
import com.bruno.reservo.repositories.BusinessRepository;
import com.bruno.reservo.repositories.ServiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ServiceEntityService {

    private final ServiceRepository repo;
    private final BusinessRepository businessRepo;
    private final UserService userService;

    public ServiceEntity create(Long businessId, ServiceEntity s) {

        Business business = businessRepo.findById(businessId)
                .orElseThrow(() -> new RuntimeException("Business not found"));

        // seguridad: solo el dueño puede agregar servicios
        if (!business.getOwner().getId().equals(userService.getLoggedUserId()))
            throw new RuntimeException("No autorizado");

        s.setBusiness(business);
        return repo.save(s);
    }

    public List<ServiceEntity> getByBusiness(Long businessId) {
        return repo.findByBusinessId(businessId);
    }

    public ServiceEntity update(Long id, ServiceEntity data) {
        ServiceEntity s = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Service not found"));

        // seguridad: solo el dueño del business puede editar el servicio
        if (!s.getBusiness().getOwner().getId().equals(userService.getLoggedUserId()))
            throw new RuntimeException("No autorizado");

        s.setName(data.getName());
        s.setDurationMinutes(data.getDurationMinutes());
        s.setPrice(data.getPrice());

        return repo.save(s);
    }

    public void delete(Long id) {
        ServiceEntity s = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Service not found"));

        if (!s.getBusiness().getOwner().getId().equals(userService.getLoggedUserId()))
            throw new RuntimeException("No autorizado");

        repo.delete(s);
    }
}
