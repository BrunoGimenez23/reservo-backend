package com.bruno.reservo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AvailableSlotDTO {
    private String time;  // Ej: "14:00"
    private boolean available;
}
