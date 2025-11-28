package com.bruno.reservo.repositories;

import com.bruno.reservo.entities.BusinessSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BusinessScheduleRepository extends JpaRepository<BusinessSchedule, Long> {

    List<BusinessSchedule> findByBusinessId(Long businessId);
    Optional<BusinessSchedule> findByBusinessIdAndDayOfWeek(Long businessId, String dayOfWeek);


}
