package com.bruno.reservo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class ReservationResponseDTO {
    private Long id;
    private String clientName;
    private String clientPhone;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String serviceName;
    private String businessName;
    private String status;
}
