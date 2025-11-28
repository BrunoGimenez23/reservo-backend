package com.bruno.reservo.controllers;

import com.bruno.reservo.services.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/public/business/{businessId}/availability")
@RequiredArgsConstructor
@CrossOrigin("*")
public class ReservationAvailabilityController {

    private final ReservationService reservationService;

    @GetMapping
    public List<String> getAvailability(
            @PathVariable Long businessId,
            @RequestParam Long serviceId,
            @RequestParam String date
    ) {
        LocalDate localDate = LocalDate.parse(date);
        return reservationService.getAvailableSlots(businessId, serviceId, localDate);
    }
}
