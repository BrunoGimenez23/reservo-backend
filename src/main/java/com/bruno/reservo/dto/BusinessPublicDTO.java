
package com.bruno.reservo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BusinessPublicDTO {
    private Long id;
    private String name;
    private String address;
    private String phone;
}
