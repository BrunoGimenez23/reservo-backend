package com.bruno.reservo.services;

import com.bruno.reservo.dto.ReservationRequestDTO;
import com.bruno.reservo.dto.ReservationResponseDTO;
import com.bruno.reservo.entities.Business;
import com.bruno.reservo.entities.Reservation;
import com.bruno.reservo.entities.ServiceEntity;
import com.bruno.reservo.entities.ReservationStatus;
import com.bruno.reservo.repositories.BusinessRepository;
import com.bruno.reservo.repositories.BusinessScheduleRepository;
import com.bruno.reservo.repositories.ReservationRepository;
import com.bruno.reservo.repositories.ServiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepo;
    private final ServiceRepository serviceRepo;
    private final BusinessRepository businessRepo;
    private final BusinessScheduleRepository scheduleRepo;

    // ==================== CREAR RESERVA ====================
    public ReservationResponseDTO create(Long businessId, ReservationRequestDTO dto) {

        ServiceEntity service = serviceRepo.findById(dto.getServiceId())
                .orElseThrow(() -> new RuntimeException("Servicio no encontrado"));

        LocalDateTime start = dto.getStartTime();
        LocalDateTime end = start.plusMinutes(service.getDurationMinutes());

        // Validación de superposición
        List<Reservation> overlapping = reservationRepo.findOverlappingReservations(
                businessId,
                start,
                end
        );

        if (!overlapping.isEmpty()) {
            throw new IllegalStateException("Ese horario ya está ocupado");
        }

        Reservation reservation = Reservation.builder()
                .clientName(dto.getClientName())
                .clientPhone(dto.getClientPhone())
                .startTime(start)
                .endTime(end)
                .service(service)
                .business(service.getBusiness())
                .status(ReservationStatus.PENDING)
                .build();

        Reservation saved = reservationRepo.save(reservation);

        return toDTO(saved);
    }

    // ==================== OBTENER RESERVAS DEL NEGOCIO ====================
    public List<ReservationResponseDTO> getByBusinessId(Long businessId) {
        return reservationRepo.findByBusinessIdOrderByStartTimeAsc(businessId)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    // ==================== CAMBIAR ESTADO ====================
    public void updateStatus(Long id, ReservationStatus newStatus) {
        Reservation res = reservationRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Reserva no encontrada"));

        res.setStatus(newStatus);
        reservationRepo.save(res);
    }

    // ==================== ELIMINAR RESERVA ====================
    public void delete(Long id) {
        reservationRepo.deleteById(id);
    }

    // ==================== DTO MAPPER ====================
    private ReservationResponseDTO toDTO(Reservation r) {
        return ReservationResponseDTO.builder()
                .id(r.getId())
                .clientName(r.getClientName())
                .clientPhone(r.getClientPhone())
                .startTime(r.getStartTime())
                .endTime(r.getEndTime())
                .serviceName(r.getService().getName())
                .businessName(r.getBusiness().getName())
                .status(r.getStatus().name())
                .build();
    }

    // ==================== DISPONIBILIDAD ====================
    public List<String> getAvailableSlots(Long businessId, Long serviceId, LocalDate date) {

        String dow = date.getDayOfWeek().name();

        var schedule = scheduleRepo
                .findByBusinessIdAndDayOfWeek(businessId, dow)
                .orElseThrow(() -> new RuntimeException("Horario no configurado"));

        if (!schedule.isActive()) return List.of(); // Día cerrado

        var service = serviceRepo.findById(serviceId)
                .orElseThrow(() -> new RuntimeException("Servicio no encontrado"));

        int duration = service.getDurationMinutes();

        LocalTime startTime = LocalTime.parse(schedule.getStartTime());
        LocalTime endTime = LocalTime.parse(schedule.getEndTime());

        if (startTime.equals(endTime)) return List.of(); // Horario inválido

        List<String> available = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime current = date.atTime(startTime);
        LocalDateTime limit = date.atTime(endTime);

        // Cargar reservas del día
        List<Reservation> busy = reservationRepo
                .findByBusinessIdAndStartTimeBetween(
                        businessId,
                        date.atStartOfDay(),
                        date.atTime(23, 59)
                ).stream()
                .filter(r -> r.getStatus() != ReservationStatus.CANCELED)
                .collect(Collectors.toList());

        while (current.plusMinutes(duration).isBefore(limit) || current.plusMinutes(duration).equals(limit)) {

            LocalDateTime slotStart = current;
            LocalDateTime slotEnd = current.plusMinutes(duration);

            boolean isPast = slotStart.isBefore(now);

            boolean isBusy = busy.stream().anyMatch(r ->
                    r.getStartTime().isBefore(slotEnd) &&
                            r.getEndTime().isAfter(slotStart)
            );

            if (!isPast && !isBusy) {
                available.add(slotStart.toLocalTime().toString().substring(0, 5));
            }

            current = current.plusMinutes(duration);
        }

        return available;
    }
}
