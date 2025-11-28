package com.bruno.reservo.dto;

import lombok.*;

@Data
public class RegisterRequest {
    private String email;
    private String password;
}
