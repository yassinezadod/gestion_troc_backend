package com.troc.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "annonces")
public class Annonce {

    @Id
    @Column(columnDefinition = "UUID")
    private UUID id = UUID.randomUUID();

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private String desiredProduct; // produit souhaité (texte libre)

    @Column
    private Double price; // si échange + argent

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AnnonceState state = AnnonceState.Active; // Active, Closed, Deleted

    // ✅ Relation ManyToOne : un utilisateur peut créer plusieurs annonces
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // ✅ Relation OneToOne : une annonce contient un seul produit
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "product_id", nullable = false, unique = true)
    private Product product;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime updatedAt = LocalDateTime.now();

    // --- Getters & Setters ---
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getDesiredProduct() { return desiredProduct; }
    public void setDesiredProduct(String desiredProduct) { this.desiredProduct = desiredProduct; }

    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }

    public AnnonceState getState() { return state; }
    public void setState(AnnonceState state) { this.state = state; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
