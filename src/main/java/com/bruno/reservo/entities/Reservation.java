package com.bruno.reservo.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String clientName;       // Nombre del cliente que reserva
    private String clientEmail;      // Email del cliente (para enviar recordatorios)

    private LocalDateTime startTime; // Fecha y hora de inicio
    private LocalDateTime endTime;   // Se calcula automáticamente

    @ManyToOne
    @JoinColumn(name = "service_id")
    private ServiceEntity service;   // Servicio reservado (corte, cancha, clase)

    @ManyToOne
    @JoinColumn(name = "business_id")
    private Business business;       // Negocio dueño de la reserva

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReservationStatus status = ReservationStatus.PENDING;


}
