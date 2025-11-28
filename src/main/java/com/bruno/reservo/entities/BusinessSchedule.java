package com.bruno.reservo.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "business_schedule")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class BusinessSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "business_id")
    private Business business;

    private String dayOfWeek; // MONDAY, TUESDAY, etc.
    private String startTime; // "09:00"
    private String endTime;   // "18:00"
    private boolean active;
}
