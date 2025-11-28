package com.bruno.reservo.controllers;

import com.bruno.reservo.dto.*;
import com.bruno.reservo.entities.*;
import com.bruno.reservo.repositories.BusinessRepository;
import com.bruno.reservo.repositories.UserRepository;
import com.bruno.reservo.security.jwt.JWTService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepo;
    private final JWTService jwtService;
    private final BCryptPasswordEncoder encoder;
    private final BusinessRepository businessRepo;


    @PostMapping("/register")
    public AuthResponse register(@RequestBody RegisterRequest req) {

        if (req.getEmail() == null || req.getPassword() == null) {
            throw new RuntimeException("Email y contraseña son requeridos");
        }

        if (userRepo.existsByEmail(req.getEmail())) {
            throw new RuntimeException("El email ya está registrado");
        }

        if (req.getPassword().length() < 6) {
            throw new RuntimeException("La contraseña debe tener mínimo 6 caracteres");
        }

        User user = User.builder()
                .email(req.getEmail())
                .password(encoder.encode(req.getPassword()))
                .role(Role.ADMIN)
                .build();

        userRepo.save(user);

        String token = jwtService.generateToken(user.getEmail());

        return new AuthResponse(token);
    }



    @PostMapping("/login")
    public AuthResponse login(@RequestBody LoginRequest req) {

        User user = userRepo.findByEmail(req.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!encoder.matches(req.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        String token = jwtService.generateToken(user.getEmail());
        return new AuthResponse(token);
    }
}
