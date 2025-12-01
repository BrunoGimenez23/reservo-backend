package com.bruno.reservo.repositories;

import com.bruno.reservo.entities.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    // Obtener reservas de un servicio en un rango de tiempo
    List<Reservation> findByServiceIdAndStartTimeBetween(Long serviceId, LocalDateTime start, LocalDateTime end);

    // Obtener todas las reservas de un día por negocio
    List<Reservation> findByBusinessIdAndStartTimeBetween(Long businessId, LocalDateTime start, LocalDateTime end);

    List<Reservation> findByBusinessIdAndServiceIdAndStartTimeBetween(
            Long businessId,
            Long serviceId,
            LocalDateTime start,
            LocalDateTime end
    );

    List<Reservation> findByBusinessIdOrderByStartTimeAsc(Long businessId);


    @Query("""
    SELECT r FROM Reservation r
    WHERE r.business.id = :businessId
    AND r.status <> 'CANCELLED'
    AND (
        (r.startTime < :newEndTime)
        AND (r.endTime > :newStartTime)
    )
""")
    List<Reservation> findOverlappingReservations(
            Long businessId,
            LocalDateTime newStartTime,
            LocalDateTime newEndTime
    );


}
