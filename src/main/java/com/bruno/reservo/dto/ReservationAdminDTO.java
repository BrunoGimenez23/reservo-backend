package com.bruno.reservo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
@AllArgsConstructor
@Data
public class ReservationAdminDTO {
    private Long id;
    private String clientName;
    private String clientEmail;
    private String serviceName;
    private String status;
    private String startTime;
}
