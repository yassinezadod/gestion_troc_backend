package com.troc.controller;


import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.troc.dto.RegisterRequest;
import com.troc.entity.User;
import com.troc.service.AuthService;

import java.net.URI;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {
        User created = authService.register(request);
        URI location = URI.create("/api/users/" + created.getId());
        return ResponseEntity.created(location)
                .body(Map.of("id", created.getId(), "email", created.getEmail()));
    }
    
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String password = request.get("password");

        String token = authService.authenticateAndGetToken(email, password);
        if (token != null) {
            return ResponseEntity.ok(Map.of("token", token, "expires_in", 3600));
        } else {
            return ResponseEntity.status(401).body(Map.of("message", "Email ou mot de passe invalide"));
        }
    }

}
