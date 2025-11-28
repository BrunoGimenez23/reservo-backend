package com.bruno.reservo.services;

import com.bruno.reservo.entities.*;
import com.bruno.reservo.repositories.*;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class BusinessScheduleService {

    private final BusinessRepository businessRepo;
    private final BusinessScheduleRepository scheduleRepo;

    private static final List<String> DAYS = Arrays.asList(
            "MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY",
            "FRIDAY", "SATURDAY", "SUNDAY"
    );

    public List<BusinessSchedule> getSchedule(Long businessId) {
        Business business = businessRepo.findById(businessId)
                .orElseThrow(() -> new RuntimeException("Negocio no encontrado"));

        List<BusinessSchedule> schedule = scheduleRepo.findByBusinessId(businessId);

        // Si no existe, inicializamos (todo apagado)
        if (schedule.isEmpty()) {
            for (String day : DAYS) {
                scheduleRepo.save(BusinessSchedule.builder()
                        .business(business)
                        .dayOfWeek(day)
                        .active(false)
                        .startTime("09:00")
                        .endTime("18:00")
                        .build());
            }
            schedule = scheduleRepo.findByBusinessId(businessId);
        }

        return schedule;
    }

    public void updateSchedule(Long businessId, List<BusinessSchedule> newSchedule) {
        Business business = businessRepo.findById(businessId)
                .orElseThrow(() -> new RuntimeException("Negocio no encontrado"));

        List<BusinessSchedule> savedSchedule = scheduleRepo.findByBusinessId(businessId);

        for (int i = 0; i < savedSchedule.size(); i++) {
            BusinessSchedule old = savedSchedule.get(i);
            BusinessSchedule updated = newSchedule.get(i);

            old.setStartTime(updated.getStartTime());
            old.setEndTime(updated.getEndTime());
            old.setActive(updated.isActive());
        }

        scheduleRepo.saveAll(savedSchedule);
    }
}
