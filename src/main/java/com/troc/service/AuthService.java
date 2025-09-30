package com.troc.service;

import com.troc.dto.RegisterRequest;
import com.troc.entity.User;
import com.troc.repository.UserRepository;
import com.troc.security.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    /**
     * Enregistrement d'un nouvel utilisateur
     */
    public User register(RegisterRequest req) {
        if (userRepository.existsByEmail(req.getEmail().toLowerCase().trim())) {
            throw new IllegalArgumentException("Email already used");
        }

        User user = new User();
        user.setEmail(req.getEmail().toLowerCase().trim());
        user.setPassword(passwordEncoder.encode(req.getPassword()));
        // autres champs peuvent être ajoutés ici
        return userRepository.save(user);
    }

    /**
     * Authentification et génération des tokens (access + refresh)
     * @param email Email de l'utilisateur
     * @param password Mot de passe en clair
     * @return Map contenant accessToken et refreshToken
     */
    public Map<String, String> authenticate(String email, String password) {
        Optional<User> userOpt = userRepository.findByEmail(email.toLowerCase().trim());

        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (passwordEncoder.matches(password, user.getPassword())) {
                // Génération des deux tokens
                String accessToken = jwtUtil.generateAccessToken(user.getEmail());
                String refreshToken = jwtUtil.generateRefreshToken(user.getEmail());

                Map<String, String> tokens = new HashMap<>();
                tokens.put("accessToken", accessToken);
                tokens.put("refreshToken", refreshToken);
                return tokens;
            }
        }
        return null; // si email ou mot de passe invalide
    }

    /**
     * Rafraîchir un accessToken à partir d’un refreshToken
     */
    public String refreshAccessToken(String refreshToken) {
        if (jwtUtil.validateToken(refreshToken)) {
            String email = jwtUtil.getEmailFromToken(refreshToken);
            return jwtUtil.generateAccessToken(email);
        }
        throw new IllegalArgumentException("Refresh token invalide ou expiré");
    }
    
    /**
     * Trouve un utilisateur par email
     */
    public User findByEmail(String email) {
        return userRepository.findByEmail(email.toLowerCase().trim())
                .orElseThrow(() -> new IllegalArgumentException("Utilisateur non trouvé"));
    }

}
