package com.bruno.reservo.dto;

import lombok.Data;

@Data
public class ServiceAdminDTO {
    private Long id;
    private String name;
    private Integer durationMinutes;
    private Double price;
}
