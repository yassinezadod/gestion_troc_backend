package com.troc.service;

import com.troc.dto.AnnonceRequest;
import com.troc.dto.AnnonceUpdateRequest;
import com.troc.entity.Annonce;
import com.troc.entity.AnnonceState;
import com.troc.entity.Product;
import com.troc.entity.User;
import com.troc.repository.AnnonceRepository;
import com.troc.repository.ProductRepository;
import com.troc.repository.UserRepository;
import com.troc.security.JwtUtil;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;
import java.util.stream.Collectors;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class AnnonceService {

    private final AnnonceRepository annonceRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public AnnonceService(AnnonceRepository annonceRepository,
                          ProductRepository productRepository,
                          UserRepository userRepository) {
        this.annonceRepository = annonceRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        
    }

    @Transactional
    public Annonce createAnnonce(AnnonceRequest request) {
        // --- Récupérer l'utilisateur connecté depuis le token JWT ---
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getName() == null) {
            throw new RuntimeException("Utilisateur non authentifié");
        }

        String email = authentication.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        // --- Vérifier que le produit existe ---
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new RuntimeException("Produit non trouvé"));

        // --- Vérifier si le produit est déjà lié à une annonce ---
        if (product.getAnnonce() != null) {
            throw new RuntimeException("Ce produit est déjà lié à une annonce");
        }

        // --- Créer l'annonce ---
        Annonce annonce = new Annonce();
        annonce.setTitle(request.getTitle());
        annonce.setDescription(request.getDescription());
        annonce.setDesiredProduct(request.getDesiredProduct());
        annonce.setPrice(request.getPrice());
        annonce.setState(request.getState() != null ? request.getState() : AnnonceState.Active);
        annonce.setUser(user);
        annonce.setProduct(product);

        // --- Sauvegarder l'annonce ---
        return annonceRepository.save(annonce);
    }
    
    // ✅ Récupérer toutes les annonces de l'utilisateur connecté avec infos user + produit
    public List<Map<String, Object>> getMyAnnonces() {
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        List<Annonce> annonces = annonceRepository.findByUser(user);

        return annonces.stream().map(a -> Map.of(
                "id", a.getId(),
                "title", a.getTitle(),
                "description", a.getDescription(),
                "desiredProduct", a.getDesiredProduct(),
                "price", a.getPrice(),
                "state", a.getState().toString(),
                "createdAt", a.getCreatedAt(),
                "updatedAt", a.getUpdatedAt(),

                // ✅ Informations sur l'utilisateur
                "user", Map.of(
                        "id", user.getId(),
                        "firstName", user.getFirstName(),
                        "lastName", user.getLastName(),
                        "email", user.getEmail(),
                        "city", user.getCity(),
                        "phoneNumber", user.getPhoneNumber(),
                        "phoneCode", user.getPhoneCode(),
                        "address", user.getAddress(),
                        "profilePicture", user.getProfilePicture()
                ),

                // ✅ Informations sur le produit
                "product", Map.of(
                        "id", a.getProduct().getId(),
                        "title", a.getProduct().getTitle(),
                        "description", a.getProduct().getDescription(),
                        "category", a.getProduct().getCategory(),
                        "state", a.getProduct().getState().toString(),
                        "yearsUsed", a.getProduct().getYearsUsed(),
                        "picture", a.getProduct().getPicture()
                )
        )).toList();
    }
    
    
    //  Récupérer toutes les annonces (publiques)
    public List<Map<String, Object>> getAllAnnonces() {
        List<Annonce> annonces = annonceRepository.findAll();

        return annonces.stream().map(a -> Map.of(
                "id", a.getId(),
                "title", a.getTitle(),
                "description", a.getDescription(),
                "desiredProduct", a.getDesiredProduct(),
                "price", a.getPrice(),
                "state", a.getState().toString(),
                "createdAt", a.getCreatedAt(),
                "updatedAt", a.getUpdatedAt(),

                // 🧑 Informations utilisateur
                "user", Map.of(
                        "id", a.getUser().getId(),
                        "firstName", a.getUser().getFirstName(),
                        "lastName", a.getUser().getLastName(),
                        "email", a.getUser().getEmail(),
                        "city", a.getUser().getCity(),
                        "phoneNumber", a.getUser().getPhoneNumber(),
                        "phoneCode", a.getUser().getPhoneCode(),
                        "address", a.getUser().getAddress(),
                        "profilePicture", a.getUser().getProfilePicture()
                ),

                // 📦 Informations produit
                "product", Map.of(
                        "id", a.getProduct().getId(),
                        "title", a.getProduct().getTitle(),
                        "description", a.getProduct().getDescription(),
                        "category", a.getProduct().getCategory(),
                        "state", a.getProduct().getState().toString(),
                        "yearsUsed", a.getProduct().getYearsUsed(),
                        "picture", a.getProduct().getPicture()
                )
        )).collect(Collectors.toList());
    }
    
 // Récupérer une annonce par ID
    public Map<String, Object> getAnnonceById(UUID id) {
        Annonce a = annonceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Annonce non trouvée avec l'id : " + id));

        return Map.of(
                "id", a.getId(),
                "title", a.getTitle(),
                "description", a.getDescription(),
                "desiredProduct", a.getDesiredProduct(),
                "price", a.getPrice(),
                "state", a.getState().toString(),
                "createdAt", a.getCreatedAt(),
                "updatedAt", a.getUpdatedAt(),

                // 🧑 Informations utilisateur
                "user", Map.of(
                        "id", a.getUser().getId(),
                        "firstName", a.getUser().getFirstName(),
                        "lastName", a.getUser().getLastName(),
                        "email", a.getUser().getEmail(),
                        "city", a.getUser().getCity(),
                        "phoneNumber", a.getUser().getPhoneNumber(),
                        "phoneCode", a.getUser().getPhoneCode(),
                        "address", a.getUser().getAddress(),
                        "profilePicture", a.getUser().getProfilePicture()
                ),

                // 📦 Informations produit
                "product", Map.of(
                        "id", a.getProduct().getId(),
                        "title", a.getProduct().getTitle(),
                        "description", a.getProduct().getDescription(),
                        "category", a.getProduct().getCategory(),
                        "state", a.getProduct().getState().toString(),
                        "yearsUsed", a.getProduct().getYearsUsed(),
                        "picture", a.getProduct().getPicture()
                )
        );
    }

    
    
    public List<Map<String, Object>> getAnnoncesByUserId(UUID userId) {
        // Vérifie si l’utilisateur existe
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé avec l'id : " + userId));

        // Récupère les annonces de cet utilisateur
        List<Annonce> annonces = annonceRepository.findByUser(user);

        // Map chaque annonce en JSON détaillé
        return annonces.stream().map(a -> Map.of(
                "id", a.getId(),
                "title", a.getTitle(),
                "description", a.getDescription(),
                "desiredProduct", a.getDesiredProduct(),
                "price", a.getPrice(),
                "state", a.getState().toString(),
                "createdAt", a.getCreatedAt(),
                "updatedAt", a.getUpdatedAt(),

                // 🧑 Informations utilisateur (résumé)
                "user", Map.of(
                        "id", user.getId(),
                        "firstName", user.getFirstName(),
                        "lastName", user.getLastName(),
                        "email", user.getEmail(),
                        "city", user.getCity(),
                        "phoneNumber", user.getPhoneNumber(),
                        "phoneCode", user.getPhoneCode(),
                        "address", user.getAddress(),
                        "profilePicture", user.getProfilePicture()
                ),

                // 📦 Informations produit
                "product", Map.of(
                        "id", a.getProduct().getId(),
                        "title", a.getProduct().getTitle(),
                        "description", a.getProduct().getDescription(),
                        "category", a.getProduct().getCategory(),
                        "state", a.getProduct().getState().toString(),
                        "yearsUsed", a.getProduct().getYearsUsed(),
                        "picture", a.getProduct().getPicture()
                )
        )).collect(Collectors.toList());
    }
    
    
    public Map<String, Object> getAnnonceByProductId(UUID productId) {
        Annonce annonce = annonceRepository.findByProductId(productId);
        if (annonce == null) return null;

        return Map.of(
            "id", annonce.getId(),
            "title", annonce.getTitle(),
            "description", annonce.getDescription(),
            "desiredProduct", annonce.getDesiredProduct(),
            "price", annonce.getPrice(),
            "state", annonce.getState().toString(),
            "createdAt", annonce.getCreatedAt(),
            "updatedAt", annonce.getUpdatedAt(),
            "user", Map.of(
                "id", annonce.getUser().getId(),
                "firstName", annonce.getUser().getFirstName(),
                "lastName", annonce.getUser().getLastName(),
                "email", annonce.getUser().getEmail()
            ),
            "product", Map.of(
                "id", annonce.getProduct().getId(),
                "title", annonce.getProduct().getTitle(),
                "description", annonce.getProduct().getDescription(),
                "category", annonce.getProduct().getCategory(),
                "state", annonce.getProduct().getState().toString(),
                "yearsUsed", annonce.getProduct().getYearsUsed(),
                "picture", annonce.getProduct().getPicture()
            )
        );
    }
    
    
    public Annonce updateAnnonce(UUID annonceId, AnnonceUpdateRequest request, UUID userId) {
        Annonce annonce = annonceRepository.findById(annonceId)
                .orElseThrow(() -> new RuntimeException("Annonce introuvable ❌"));

        // Vérifier que l'utilisateur connecté est bien le propriétaire
        if (!annonce.getUser().getId().equals(userId)) {
            throw new RuntimeException("Vous n'êtes pas autorisé à modifier cette annonce ");
        }

        // Mise à jour partielle des champs
        if (request.getTitle() != null) annonce.setTitle(request.getTitle());
        if (request.getDescription() != null) annonce.setDescription(request.getDescription());
        if (request.getDesiredProduct() != null) annonce.setDesiredProduct(request.getDesiredProduct());
        if (request.getPrice() != null) annonce.setPrice(request.getPrice());
        if (request.getState() != null) annonce.setState(request.getState());

        // Vérification du produit avant mise à jour
        if (request.getProductId() != null) {
            UUID productId = UUID.fromString(request.getProductId());
            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new RuntimeException("Produit introuvable"));

            // Vérifier si ce produit est déjà associé à une autre annonce
            Optional<Annonce> otherAnnonce = annonceRepository.findByProduct(product);
            if (otherAnnonce.isPresent() && !otherAnnonce.get().getId().equals(annonceId)) {
                throw new RuntimeException("Ce produit est déjà associé à une autre annonce");
            }

            annonce.setProduct(product);
        }


        annonce.setUpdatedAt(java.time.LocalDateTime.now());

        return annonceRepository.save(annonce);
    }


}
