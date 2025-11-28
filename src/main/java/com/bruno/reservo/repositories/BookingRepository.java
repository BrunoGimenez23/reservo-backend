package com.bruno.reservo.repositories;

import com.bruno.reservo.entities.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByService_Business_Id(Long businessId);

    List<Booking> findByDateAndService_Id(LocalDate date, Long serviceId);
}
