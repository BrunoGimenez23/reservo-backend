package com.bruno.reservo.repositories;

import com.bruno.reservo.entities.Business;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BusinessRepository extends JpaRepository<Business, Long> {
    List<Business> findByOwnerId(Long ownerId);
    Optional<Business> findBySlug(String slug);


}
