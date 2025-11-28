package com.bruno.reservo.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServiceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private Integer durationMinutes;
    private Double price;

    @ManyToOne
    @JoinColumn(name = "business_id")
    private Business business;
}
