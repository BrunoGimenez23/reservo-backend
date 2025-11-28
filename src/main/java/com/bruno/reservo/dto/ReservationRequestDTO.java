package com.bruno.reservo.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ReservationRequestDTO {
    private String clientName;
    private String clientEmail;
    private LocalDateTime startTime;
    private Long serviceId;
}
