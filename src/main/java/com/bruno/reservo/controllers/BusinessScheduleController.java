package com.bruno.reservo.controllers;

import com.bruno.reservo.entities.BusinessSchedule;
import com.bruno.reservo.services.BusinessScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/business/{businessId}/schedule")
@RequiredArgsConstructor
public class BusinessScheduleController {

    private final BusinessScheduleService service;

    @GetMapping
    public List<BusinessSchedule> getSchedule(@PathVariable Long businessId) {
        return service.getSchedule(businessId);
    }

    @PutMapping
    public void updateSchedule(
            @PathVariable Long businessId,
            @RequestBody List<BusinessSchedule> schedule
    ) {
        service.updateSchedule(businessId, schedule);
    }
}
