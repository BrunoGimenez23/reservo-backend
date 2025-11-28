package com.bruno.reservo.controllers;

import com.bruno.reservo.entities.Business;
import com.bruno.reservo.services.BusinessService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/business")
@RequiredArgsConstructor
public class BusinessAdminController {

    private final BusinessService businessService;

    @PostMapping
    public Business create(@RequestBody Business data) {
        return businessService.create(data);
    }

    @GetMapping
    public List<Business> getMyBusinesses() {
        return businessService.getMyBusinesses();
    }

    @GetMapping("/{id}")
    public Business getOne(@PathVariable Long id) {
        return businessService.getOne(id);
    }

    @PutMapping("/{id}")
    public Business update(@PathVariable Long id, @RequestBody Business data) {
        return businessService.update(id, data);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        businessService.delete(id);
    }
}
