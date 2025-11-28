package com.bruno.reservo.controllers;

import com.bruno.reservo.dto.ReservationRequestDTO;
import com.bruno.reservo.dto.ReservationResponseDTO;
import com.bruno.reservo.entities.ReservationStatus;
import com.bruno.reservo.services.BusinessService;
import com.bruno.reservo.services.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/business/{businessId}/reservations")
@RequiredArgsConstructor
public class ReservationAdminController {

    private final ReservationService reservationService;
    private final BusinessService businessService;

    @GetMapping
    public List<ReservationResponseDTO> getReservations(@PathVariable Long businessId) {
        businessService.validateOwnership(businessId);
        return reservationService.getByBusinessId(businessId);
    }

    @PostMapping
    public ResponseEntity<ReservationResponseDTO> create(
            @PathVariable Long businessId,
            @RequestBody ReservationRequestDTO dto
    ) {
        businessService.validateOwnership(businessId);
        return ResponseEntity.ok(reservationService.create(businessId, dto));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> updateStatus(
            @PathVariable Long businessId,
            @PathVariable Long id,
            @RequestParam ReservationStatus status
    ) {
        businessService.validateOwnership(businessId);
        reservationService.updateStatus(id, status);
        return ResponseEntity.noContent().build();
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long businessId,
            @PathVariable Long id
    ) {
        businessService.validateOwnership(businessId);
        reservationService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
