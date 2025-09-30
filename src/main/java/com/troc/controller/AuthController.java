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

        // Authentification via AuthService
        Map<String, String> tokens = authService.authenticate(email, password);

        // ❌ Mauvais identifiants
        if (tokens == null) {
            return ResponseEntity.status(401).body(Map.of(
                    "type", "Error",
                    "message", "Email ou mot de passe invalide",
                    "redirect", "registerpage"
            ));
        }

        // ✅ Authentification réussie → récupérer l'utilisateur
        User user = authService.findByEmail(email);

        boolean incompleteProfile = (user.getFirstName() == null || user.getLastName() == null ||
                user.getPhoneCode() == null || user.getPhoneNumber() == null ||
                user.getCity() == null || user.getAddress() == null);

        // Retourner toujours 200 OK avec le token, même si profil incomplet
        Map<String, Object> response = Map.of(
                "type", incompleteProfile ? "InfoNeeded" : "Success",
                "message", incompleteProfile ? "Please complete your information" :
                        "Your request was processed successfully.",
                "redirect", incompleteProfile ? "completeinfopage" : "homepage",
                "accessToken", tokens.get("accessToken"),
                "refreshToken", tokens.get("refreshToken")
        );

        return ResponseEntity.ok(response);
    }




    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestBody Map<String, String> request) {
        String refreshToken = request.get("refreshToken");
        try {
            String newAccessToken = authService.refreshAccessToken(refreshToken);
            return ResponseEntity.ok(Map.of("accessToken", newAccessToken, "expires_in", 3600));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(401).body(Map.of("message", "Refresh token invalide ou expiré"));
        }
    }
}
