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

        Business business = businessRepo.findById(businessId)
                .orElseThrow(() -> new RuntimeException("Business not found"));

        ServiceEntity service = serviceRepo.findById(dto.getServiceId())
                .orElseThrow(() -> new RuntimeException("Service not found"));

        LocalDateTime start = dto.getStartTime();
        LocalDateTime end = start.plusMinutes(service.getDurationMinutes());

        // Ver si se solapa con otras reservas del negocio
        var overlaps = reservationRepo.findByBusinessIdAndStartTimeBetween(
                businessId,
                start.minusMinutes(service.getDurationMinutes()),
                end
        );

        boolean conflict = overlaps.stream().anyMatch(r ->
                r.getStartTime().isBefore(end) &&
                        r.getEndTime().isAfter(start)
        );

        if (conflict) {
            throw new RuntimeException("Horario ocupado");
        }

        Reservation r = Reservation.builder()
                .clientName(dto.getClientName())
                .clientEmail(dto.getClientEmail())
                .startTime(start)
                .endTime(end)
                .service(service)
                .business(business)
                .status(ReservationStatus.PENDING)
                .build();

        reservationRepo.save(r);
        return toDTO(r);
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
        ReservationResponseDTO dto = new ReservationResponseDTO();
        dto.setId(r.getId());
        dto.setClientName(r.getClientName());
        dto.setClientEmail(r.getClientEmail());
        dto.setStartTime(r.getStartTime());
        dto.setEndTime(r.getEndTime());
        dto.setServiceName(r.getService().getName());
        dto.setBusinessName(r.getBusiness().getName());
        dto.setStatus(r.getStatus().name());
        return dto;
    }


    // ==================== DISPONIBILIDAD ====================
    public List<String> getAvailableSlots(Long businessId, Long serviceId, LocalDate date) {

        String dow = date.getDayOfWeek().name();

        var schedule = scheduleRepo
                .findByBusinessIdAndDayOfWeek(businessId, dow)
                .orElseThrow(() -> new RuntimeException("Horario no configurado"));

        if (!schedule.isActive()) return List.of(); // Día cerrado

        var service = serviceRepo.findById(serviceId)
                .orElseThrow(() -> new RuntimeException("Service not found"));

        int duration = service.getDurationMinutes();

        LocalTime start = LocalTime.parse(schedule.getStartTime());
        LocalTime end = LocalTime.parse(schedule.getEndTime());

        if (start.equals(end)) return List.of(); // Horario inválido

        List<String> available = new ArrayList<>();

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime current = date.atTime(start);
        LocalDateTime limit = date.atTime(end);

        List<Reservation> busy = reservationRepo
                .findByBusinessIdAndServiceIdAndStartTimeBetween(
                        businessId,
                        serviceId,
                        date.atStartOfDay(),
                        date.atTime(23, 59)
                );

        while (current.plusMinutes(duration).compareTo(limit) <= 0) {

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
