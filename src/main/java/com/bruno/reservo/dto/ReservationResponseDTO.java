package com.bruno.reservo.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ReservationResponseDTO {
    private Long id;
    private String clientName;
    private String clientEmail;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String serviceName;
    private String businessName;
    private String status;
}
