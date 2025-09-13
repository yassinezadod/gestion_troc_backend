package com.troc.service;

import com.troc.dto.RegisterRequest;
import com.troc.entity.User;
import com.troc.repository.UserRepository;
import com.troc.security.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
     * Authentification et génération d'un token JWT
     * @param email Email de l'utilisateur
     * @param password Mot de passe en clair
     * @return JWT si identifiants corrects, sinon null
     */
    public String authenticateAndGetToken(String email, String password) {
        Optional<User> userOpt = userRepository.findByEmail(email.toLowerCase().trim());
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (passwordEncoder.matches(password, user.getPassword())) {
                // Génère un JWT avec expiration 1h
                return jwtUtil.generateToken(user.getEmail());
            }
        }
        return null; // si email ou mot de passe invalide
    }
}
