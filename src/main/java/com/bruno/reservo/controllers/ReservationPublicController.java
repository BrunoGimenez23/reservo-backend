package com.bruno.reservo.controllers;

import com.bruno.reservo.dto.ReservationRequestDTO;
import com.bruno.reservo.dto.ReservationResponseDTO;
import com.bruno.reservo.services.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/public/business/{businessId}/reservations")
@RequiredArgsConstructor
@CrossOrigin("*")
public class ReservationPublicController {

    private final ReservationService reservationService;

    @PostMapping
    public ResponseEntity<ReservationResponseDTO> createPublic(
            @PathVariable Long businessId,
            @RequestBody ReservationRequestDTO dto
    ) {
        return ResponseEntity.ok(reservationService.create(businessId, dto));
    }
}
