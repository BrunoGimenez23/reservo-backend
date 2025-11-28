package com.bruno.reservo.services;

import com.bruno.reservo.entities.Business;
import com.bruno.reservo.repositories.BusinessRepository;
import com.bruno.reservo.repositories.ServiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BusinessService {

    private final BusinessRepository repo;
    private final ServiceRepository serviceRepo;
    private final UserService userService;

    public List<Business> getMyBusinesses() {
        return repo.findByOwnerId(userService.getLoggedUserId());
    }

    public Business create(Business b) {
        b.setId(null);
        b.setOwner(userService.getLoggedUser());
        Business saved = repo.save(b);

        String slug = toSlug(saved.getName()) + "-" + saved.getId();
        saved.setSlug(slug);
        return repo.save(saved);
    }

    public Business update(Long id, Business newData) {
        Business b = getMyBusinessById(id);
        b.setName(newData.getName());
        b.setAddress(newData.getAddress());
        b.setPhone(newData.getPhone());
        return repo.save(b);
    }

    public void delete(Long id) {
        Business b = getMyBusinessById(id);
        repo.delete(b);
    }

    public Business getOne(Long id) {
        return getMyBusinessById(id);
    }

    public Business getMyBusinessById(Long id) {
        Business b = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Negocio no encontrado"));
        validateOwnership(b);
        return b;
    }

    public Business getBySlug(String slug) {
        return repo.findBySlug(slug)
                .orElseThrow(() -> new RuntimeException("Negocio no encontrado"));
    }

    // 🔐 Validar dueño usando objeto Business
    public void validateOwnership(Business b) {
        if (!b.getOwner().getId().equals(userService.getLoggedUserId())) {
            throw new RuntimeException("No autorizado");
        }
    }

    // 🔐 Validar dueño usando businessId
    public void validateOwnership(Long businessId) {
        Business b = repo.findById(businessId)
                .orElseThrow(() -> new RuntimeException("Negocio no encontrado"));
        validateOwnership(b);
    }

    // 🔐 Validar dueño usando serviceId
    public void validateOwnershipByService(Long serviceId) {
        var service = serviceRepo.findById(serviceId)
                .orElseThrow(() -> new RuntimeException("Servicio no encontrado"));

        validateOwnership(service.getBusiness());
    }

    private String toSlug(String name) {
        return name.toLowerCase()
                .replaceAll("[^a-z0-9]+", "-")
                .replaceAll("^-|-$", "");
    }
}
