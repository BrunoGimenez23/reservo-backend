package com.bruno.reservo.repositories;

import com.bruno.reservo.entities.ServiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ServiceRepository extends JpaRepository<ServiceEntity, Long> {
    List<ServiceEntity> findByBusinessId(Long businessId);
}
