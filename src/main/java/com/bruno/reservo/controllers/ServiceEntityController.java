package com.bruno.reservo.controllers;

import com.bruno.reservo.entities.ServiceEntity;
import com.bruno.reservo.services.ServiceEntityService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/services")
@RequiredArgsConstructor
public class ServiceEntityController {

    private final ServiceEntityService service;

    @PostMapping("/{businessId}")
    public ServiceEntity create(@PathVariable Long businessId, @RequestBody ServiceEntity s) {
        return service.create(businessId, s);
    }

    @GetMapping("/by-business/{id}")
    public List<ServiceEntity> getByBusiness(@PathVariable Long id) {
        return service.getByBusiness(id);
    }

    @PutMapping("/{id}")
    public ServiceEntity update(@PathVariable Long id, @RequestBody ServiceEntity s) {
        return service.update(id, s);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
