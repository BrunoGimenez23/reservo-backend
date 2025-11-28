package com.bruno.reservo.controllers;

import com.bruno.reservo.entities.Reservation;
import com.bruno.reservo.entities.ServiceEntity;
import com.bruno.reservo.repositories.ReservationRepository;
import com.bruno.reservo.repositories.ServiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

@RestController
@RequestMapping("/reservations/slots")
@RequiredArgsConstructor
public class ReservationSlotsController {

    private final ReservationRepository reservationRepo;
    private final ServiceRepository serviceRepo;

    @GetMapping("/{businessId}/{serviceId}")
    public List<Map<String, Object>> getSlots(
            @PathVariable Long businessId,
            @PathVariable Long serviceId,
            @RequestParam String date
    ) {

        ServiceEntity service = serviceRepo.findById(serviceId)
                .orElseThrow(() -> new RuntimeException("Service not found"));

        int duration = service.getDurationMinutes();

        LocalDate d = LocalDate.parse(date);

        LocalDateTime startOfDay = d.atTime(8, 0);
        LocalDateTime endOfDay   = d.atTime(20, 0);

        List<Reservation> reservations = reservationRepo
                .findByBusinessIdAndServiceIdAndStartTimeBetween(
                        businessId, serviceId, startOfDay, endOfDay
                );

        Set<String> occupied = new HashSet<>();
        for (Reservation r : reservations) {
            occupied.add(r.getStartTime().toLocalTime().toString().substring(0,5));
        }

        List<Map<String, Object>> slots = new ArrayList<>();

        LocalDateTime time = startOfDay;
        while (!time.isAfter(endOfDay.minusMinutes(duration))) {

            String display = time.toLocalTime().toString().substring(0, 5);
            boolean available = !occupied.contains(display);

            slots.add(Map.of(
                    "time", display,
                    "available", available
            ));

            time = time.plusMinutes(duration);
        }

        return slots;
    }
}
