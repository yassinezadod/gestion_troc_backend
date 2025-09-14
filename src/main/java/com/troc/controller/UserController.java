package com.troc.controller;

import com.troc.dto.UserResponse;
import com.troc.dto.UserUpdateRequest;
import com.troc.entity.User;
import com.troc.repository.UserRepository;
import com.troc.security.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Optional;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    public UserController(JwtUtil jwtUtil, UserRepository userRepository) {
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
    }

    /**
     * Retourne toutes les informations de l'utilisateur connecté
     */
    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body("Token manquant");
        }

        String token = authHeader.substring(7); // enlève "Bearer "
        if (!jwtUtil.validateToken(token)) {
            return ResponseEntity.status(401).body("Token invalide ou expiré");
        }

        String email = jwtUtil.getEmailFromToken(token);
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(404).body("Utilisateur non trouvé");
        }

        User user = userOpt.get();
        return ResponseEntity.ok(user); // retourne tout l'objet User
    }
    
    /**
     * modifier les informations de l'utilisateur connecté
     */
    
    
    @PatchMapping("/me")
    public ResponseEntity<?> updateCurrentUser(
            HttpServletRequest request,
            @RequestBody UserUpdateRequest updateRequest) {

        // Récupération du token
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body("Token manquant");
        }

        String token = authHeader.substring(7);
        if (!jwtUtil.validateToken(token)) {
            return ResponseEntity.status(401).body("Token invalide ou expiré");
        }

        String email = jwtUtil.getEmailFromToken(token);
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(404).body("Utilisateur non trouvé");
        }

        User user = userOpt.get();

        // Mise à jour partielle
        if (updateRequest.getFirstName() != null) user.setFirstName(updateRequest.getFirstName());
        if (updateRequest.getLastName() != null) user.setLastName(updateRequest.getLastName());
        if (updateRequest.getPhoneCode() != null) user.setPhoneCode(updateRequest.getPhoneCode());
        if (updateRequest.getPhoneNumber() != null) user.setPhoneCode(updateRequest.getPhoneNumber());
        if (updateRequest.getCity() != null) user.setCity(updateRequest.getCity());
        if (updateRequest.getAddress() != null) user.setAddress(updateRequest.getAddress());
        if (updateRequest.getProfilePicture() != null) user.setProfilePicture(updateRequest.getProfilePicture());

        userRepository.save(user);

        // Retourner l’utilisateur mis à jour via DTO
     // Retourner l’utilisateur mis à jour via DTO
        UserResponse response = new UserResponse(
                user.getId(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getPhoneCode(),
                user.getPhoneNumber(),
                user.getCity(),
                user.getAddress(),
                user.getProfilePicture(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );


        return ResponseEntity.ok(response);
    }
    
    /**
     * Suprimer le compte de l'utilisateur connecté
     */
    
    
    @DeleteMapping("/me")
    public ResponseEntity<?> deleteCurrentUser(HttpServletRequest request) {
        // Récupération du token
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body("Token manquant");
        }

        String token = authHeader.substring(7);
        if (!jwtUtil.validateToken(token)) {
            return ResponseEntity.status(401).body("Token invalide ou expiré");
        }

        // Récupérer l'utilisateur via le token
        String email = jwtUtil.getEmailFromToken(token);
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(404).body("Utilisateur non trouvé");
        }

        // Supprimer l'utilisateur
        userRepository.delete(userOpt.get());

        return ResponseEntity.ok("Compte utilisateur supprimé avec succès");
    }


}
