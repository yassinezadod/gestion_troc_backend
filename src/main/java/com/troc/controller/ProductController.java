package com.troc.controller;

import com.troc.dto.ProductRequest;
import com.troc.entity.Product;
import com.troc.entity.User;
import com.troc.repository.ProductRepository;
import com.troc.repository.UserRepository;
import com.troc.service.ProductService;
import com.troc.security.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.troc.dto.ProductResponse;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;

import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final ProductService productService; 
    private final JwtUtil jwtUtil;
    

    public ProductController(ProductRepository productRepository,
                             UserRepository userRepository,
                             JwtUtil jwtUtil,
                             ProductService productService) {
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.productService = productService;
    }

    /**
     * Ajouter un produit pour l'utilisateur connecté
     */
    @PostMapping
    public ResponseEntity<?> addProduct(@RequestBody ProductRequest request,
                                        HttpServletRequest httpRequest) {

        //  Récupérer le token depuis l'entête
        String authHeader = httpRequest.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body("Token manquant");
        }
        String token = authHeader.substring(7);

        //  Valider le token
        if (!jwtUtil.validateToken(token)) {
            return ResponseEntity.status(401).body("Token invalide ou expiré");
        }

        //  Extraire l'utilisateur
        String email = jwtUtil.getEmailFromToken(token);
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(404).body("Utilisateur non trouvé");
        }
        User owner = userOpt.get();

        //  Créer le produit
        Product product = new Product();
        product.setTitle(request.getTitle());
        product.setDescription(request.getDescription());
        product.setCategory(request.getCategory());
        product.setState(request.getState());
        product.setYearsUsed(request.getYearsUsed());
        product.setPicture(request.getPicture());
        product.setOwner(owner);
        product.setCreatedAt(LocalDateTime.now());
        product.setUpdatedAt(LocalDateTime.now());

        productRepository.save(product);

        return ResponseEntity.ok(product);
    }
    
    /**
     * Voir tous les produits 
     */
    
    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAllProducts() {
        List<ProductResponse> products = productRepository.findAll().stream()
            .map(product -> {
                var owner = product.getOwner();
                return new ProductResponse(
                    product.getId(),
                    product.getTitle(),
                    product.getDescription(),
                    product.getCategory(),
                    product.getState(),
                    product.getYearsUsed(),
                    product.getPicture(),
                    owner != null ? owner.getId() : null,
                    owner != null ? owner.getEmail() : null,
                    owner != null ? owner.getFirstName() : null,
                    owner != null ? owner.getLastName() : null,
                    owner != null ? owner.getPhoneCode() : null,
                    owner != null ? owner.getPhoneNumber() : null,
                    owner != null ? owner.getCity() : null,
                    owner != null ? owner.getAddress() : null,
                    owner != null ? owner.getProfilePicture() : null,
                    product.getCreatedAt(),
                    product.getUpdatedAt()
                );
            })
            .toList();

        return ResponseEntity.ok(products);
    }
    
    /**
     * voir le produit par utilisateur 
     */
    
    
    @GetMapping("/me")
    public ResponseEntity<?> getMyProducts(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body("Token manquant");
        }

        String token = authHeader.substring(7); // enlève "Bearer "
        if (!jwtUtil.validateToken(token)) {
            return ResponseEntity.status(401).body("Token invalide ou expiré");
        }

        String email = jwtUtil.getEmailFromToken(token);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        List<ProductResponse> products = productRepository.findByOwner(user).stream()
                .map(product -> new ProductResponse(
                        product.getId(),
                        product.getTitle(),
                        product.getDescription(),
                        product.getCategory(),
                        product.getState(),
                        product.getYearsUsed(),
                        product.getPicture(),
                        user.getId(),
                        user.getEmail(),
                        user.getFirstName(),
                        user.getLastName(),
                        user.getPhoneCode(),
                        user.getPhoneNumber(),
                        user.getCity(),
                        user.getAddress(),
                        user.getProfilePicture(),
                        product.getCreatedAt(),
                        product.getUpdatedAt()
                ))
                .toList();

        return ResponseEntity.ok(products);
    }
    
    /**
     * voir le produit par ID
     */


    @GetMapping("/{id}")
    public ResponseEntity<?> getProductById(@PathVariable("id") java.util.UUID id) {
        Optional<Product> productOpt = productRepository.findById(id);

        if (productOpt.isEmpty()) {
            return ResponseEntity.status(404).body("Produit non trouvé");
        }

        Product product = productOpt.get();
        User owner = product.getOwner();

        ProductResponse response = new ProductResponse(
            product.getId(),
            product.getTitle(),
            product.getDescription(),
            product.getCategory(),
            product.getState(),
            product.getYearsUsed(),
            product.getPicture(),
            owner != null ? owner.getId() : null,
            owner != null ? owner.getEmail() : null,
            owner != null ? owner.getFirstName() : null,
            owner != null ? owner.getLastName() : null,
            owner != null ? owner.getPhoneCode() : null,
            owner != null ? owner.getPhoneNumber() : null,
            owner != null ? owner.getCity() : null,
            owner != null ? owner.getAddress() : null,
            owner != null ? owner.getProfilePicture() : null,
            product.getCreatedAt(),
            product.getUpdatedAt()
        );

        return ResponseEntity.ok(response);
    }
    
    /**
     * Lister les produits d’un utilisateur
     */
    
    
    @GetMapping(params = "ownerId")
    public ResponseEntity<?> getProductsByOwner(@RequestParam("ownerId") java.util.UUID ownerId) {
        Optional<User> userOpt = userRepository.findById(ownerId);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(404).body("Utilisateur non trouvé");
        }

        User owner = userOpt.get();

        List<ProductResponse> products = productRepository.findByOwner(owner).stream()
                .map(product -> new ProductResponse(
                        product.getId(),
                        product.getTitle(),
                        product.getDescription(),
                        product.getCategory(),
                        product.getState(),
                        product.getYearsUsed(),
                        product.getPicture(),
                        owner.getId(),
                        owner.getEmail(),
                        owner.getFirstName(),
                        owner.getLastName(),
                        owner.getPhoneCode(),
                        owner.getPhoneNumber(),
                        owner.getCity(),
                        owner.getAddress(),
                        owner.getProfilePicture(),
                        product.getCreatedAt(),
                        product.getUpdatedAt()
                ))
                .toList();

        return ResponseEntity.ok(products);
    }
    
    
    /**
     * Supprimer un produit par utilisateur  
     */
 
    @DeleteMapping("/me/{id}")
    public ResponseEntity<?> deleteMyProduct(@PathVariable("id") java.util.UUID productId,
                                             HttpServletRequest request) {
        // Vérifier le token JWT
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body("Token manquant");
        }

        String token = authHeader.substring(7);
        if (!jwtUtil.validateToken(token)) {
            return ResponseEntity.status(401).body("Token invalide ou expiré");
        }

        // Extraire l’utilisateur à partir du token
        String email = jwtUtil.getEmailFromToken(token);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        //  Vérifier si le produit existe
        Optional<Product> productOpt = productRepository.findById(productId);
        if (productOpt.isEmpty()) {
            return ResponseEntity.status(404).body("Produit non trouvé");
        }

        Product product = productOpt.get();

        //  Vérifier que l’utilisateur est bien le propriétaire du produit
        if (!product.getOwner().getId().equals(user.getId())) {
            return ResponseEntity.status(403).body("Accès refusé : vous n’êtes pas le propriétaire de ce produit");
        }

        // Supprimer le produit
        productRepository.delete(product);

        return ResponseEntity.ok("Produit supprimé avec succès ✅");
    }
    
    /**
     * Supprimer tous les produits par utilisateur
     */
    
    
    @DeleteMapping("/me")
    public ResponseEntity<?> deleteAllMyProducts(HttpServletRequest request) {
        //  Vérifier la présence du token JWT
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body("Token manquant");
        }

        String token = authHeader.substring(7);
        if (!jwtUtil.validateToken(token)) {
            return ResponseEntity.status(401).body("Token invalide ou expiré");
        }

        //  Extraire l'utilisateur courant depuis le token
        String email = jwtUtil.getEmailFromToken(token);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        //  Récupérer tous les produits de cet utilisateur
        List<Product> products = productRepository.findByOwner(user);
        if (products.isEmpty()) {
            return ResponseEntity.status(404).body("Aucun produit trouvé pour cet utilisateur");
        }

        //  Supprimer tous les produits de l'utilisateur
        productRepository.deleteAll(products);

        return ResponseEntity.ok("Tous vos produits ont été supprimés avec succès ✅");
    }
    
    
    @PatchMapping("/{id}")
    public ResponseEntity<?> updateProductPartially(
            @PathVariable UUID id,
            @RequestBody Map<String, Object> updates,
            HttpServletRequest request) {

        // Récupérer le token depuis l'en-tête
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body("Token manquant");
        }
        String token = authHeader.substring(7);

        // Valider le token
        if (!jwtUtil.validateToken(token)) {
            return ResponseEntity.status(401).body("Token invalide ou expiré");
        }

        // Extraire l'email de l'utilisateur
        String email = jwtUtil.getEmailFromToken(token);

        try {
            // Mettre à jour partiellement le produit
            Product updatedProduct = productService.updateProductPartially(id, updates, email);
            return ResponseEntity.ok(updatedProduct);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body("Valeur de champ invalide : " + e.getMessage());
        } catch (RuntimeException e) { // toutes les autres RuntimeException
            return ResponseEntity.status(403).body(e.getMessage());
        }
    }







}
