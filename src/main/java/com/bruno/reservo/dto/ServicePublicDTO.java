package com.bruno.reservo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ServicePublicDTO {
    private Long id;
    private String name;
    private Double price;
    private Integer durationMinutes;
}
