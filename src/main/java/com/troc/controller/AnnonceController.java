package com.troc.controller;

import com.troc.dto.AnnonceRequest;
import com.troc.dto.AnnonceUpdateRequest;
import com.troc.entity.Annonce;
import com.troc.entity.User;
import com.troc.repository.AnnonceRepository;
import com.troc.repository.UserRepository;
import com.troc.service.AnnonceService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.troc.security.JwtUtil;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/annonces")
public class AnnonceController {

    private final AnnonceService annonceService;
    private final AnnonceRepository annonceRepository;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    public AnnonceController(AnnonceService annonceService,AnnonceRepository annonceRepository,UserRepository userRepository,  JwtUtil jwtUtil) {
        this.annonceService = annonceService;
        this.annonceRepository = annonceRepository;
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        
    }

    // --- Créer une annonce ---
    @PostMapping
    public ResponseEntity<Map<String, String>> createAnnonce(@Valid @RequestBody AnnonceRequest request) {
        try {
            annonceService.createAnnonce(request); // On n'a pas besoin de retourner l'objet
            Map<String, String> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Annonce créée avec succès");
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("status", "error");
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }
    
    
    @GetMapping("/me")
    public ResponseEntity<?> getMyAnnonces() {
        try {
            return ResponseEntity.ok(annonceService.getMyAnnonces());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("status", "error", "message", e.getMessage()));
        }
    }
    
    
 // ✅ Voir toutes les annonces (publique)
    @GetMapping
    public ResponseEntity<?> getAllAnnonces() {
        try {
            return ResponseEntity.ok(annonceService.getAllAnnonces());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("status", "error", "message", e.getMessage()));
        }
    }
    
    
    
    @GetMapping("/{id}")
    public ResponseEntity<?> getAnnonceById(@PathVariable UUID id) {
        try {
            Map<String, Object> annonce = annonceService.getAnnonceById(id);
            return ResponseEntity.ok(Map.of(
                    "annonce", annonce
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // GET /api/annonces?userId=xxxx → annonces d’un utilisateur précis
    @GetMapping(params = "userId")
    public ResponseEntity<?> getAnnoncesByUserId(@RequestParam UUID userId) {
        var annonces = annonceService.getAnnoncesByUserId(userId);
        return ResponseEntity.ok(Map.of(
            "message", "Annonces de l'utilisateur récupérées ✅",
            "annonces", annonces
        ));
    }
    
    // GET /api/annonces?productId={id} — annonce liée à un produit spécifique
    @GetMapping(params = "productId")
    public ResponseEntity<?> getAnnonceByProductId(@RequestParam UUID productId) {
        var annonce = annonceService.getAnnonceByProductId(productId);
        if (annonce == null) {
            return ResponseEntity.status(404).body(Map.of("error", "Aucune annonce trouvée pour ce produit ❌"));
        }
        return ResponseEntity.ok(Map.of(
            "message", "Annonce du produit récupérée ✅",
            "annonce", annonce
        ));
    }
    
    
 // ------------------- PATCH /api/annonces/{id} -------------------
    @PatchMapping("/{id}")
    public ResponseEntity<?> updateAnnonce(
            @PathVariable UUID id,
            @RequestBody AnnonceUpdateRequest request,
            HttpServletRequest httpRequest) {

        // Récupérer le token JWT depuis l'en-tête
        String authHeader = httpRequest.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body("Token manquant");
        }

        String token = authHeader.substring(7);

        // Valider le token
        if (!jwtUtil.validateToken(token)) {
            return ResponseEntity.status(401).body("Token invalide ou expiré");
        }

        // Extraire l'utilisateur à partir du token
        String email = jwtUtil.getEmailFromToken(token);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        try {
            // Appeler le service pour mettre à jour partiellement
            Annonce updatedAnnonce = annonceService.updateAnnonce(id, request, user.getId());
            return ResponseEntity.ok(updatedAnnonce);
        } catch (RuntimeException e) {
            // Gestion des erreurs : pas autorisé ou annonce introuvable
            return ResponseEntity.status(403).body(e.getMessage());
        }
    }
    
    
    
    @DeleteMapping("/me/{id}")
    public ResponseEntity<?> deleteAnnonce(
            @PathVariable UUID id,
            HttpServletRequest request) {

        // 🧠 1. Récupérer et vérifier le token JWT
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body("Token manquant ❌");
        }

        String token = authHeader.substring(7);
        if (!jwtUtil.validateToken(token)) {
            return ResponseEntity.status(401).body("Token invalide ou expiré ❌");
        }

        // 🧑‍💻 2. Identifier l'utilisateur connecté
        String email = jwtUtil.getEmailFromToken(token);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé ❌"));

        // 🔍 3. Vérifier que l’annonce existe
        Optional<Annonce> annonceOpt = annonceRepository.findById(id);
        if (annonceOpt.isEmpty()) {
            return ResponseEntity.status(404).body("Annonce introuvable ❌");
        }

        Annonce annonce = annonceOpt.get();

        // 🔐 4. Vérifier que l'utilisateur est le propriétaire
        if (!annonce.getUser().getId().equals(user.getId())) {
            return ResponseEntity.status(403).body("Accès refusé : vous n’êtes pas le propriétaire de cette annonce 🚫");
        }

        // 🗑️ 5. Supprimer l’annonce
        annonceRepository.delete(annonce);

        return ResponseEntity.ok(Map.of(
                "message", "Annonce supprimée avec succès ✅",
                "deletedAnnonceId", annonce.getId()
        ));
    }
    
    
    @DeleteMapping
    public ResponseEntity<String> deleteAllAnnonces() {
        annonceRepository.deleteAllAnnoncesForce();
        return ResponseEntity.ok("Toutes les annonces ont été supprimées avec succès.");
    }




}
