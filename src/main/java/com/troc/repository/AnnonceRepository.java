package com.troc.repository;

import com.troc.entity.Annonce;
import com.troc.entity.User;

import jakarta.transaction.Transactional;

import com.troc.entity.AnnonceState;
import com.troc.entity.Product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AnnonceRepository extends JpaRepository<Annonce, UUID> {

    // Trouver toutes les annonces créées par un utilisateur spécifique
    List<Annonce> findByUser(User user);

    // Trouver les annonces par état (Active, Closed, Deleted)
    List<Annonce> findByState(AnnonceState state);

    // Rechercher les annonces contenant un mot-clé dans le titre
    List<Annonce> findByTitleContainingIgnoreCase(String title);

    // Rechercher les annonces où le produit souhaité contient un mot clé
    List<Annonce> findByDesiredProductContainingIgnoreCase(String keyword);

    // Trouver une annonce par produit (utile pour vérifier si un produit est déjà lié à une annonce)
    Annonce findByProductId(UUID productId);
    
    Optional<Annonce> findByProduct(Product product);
    
    @Transactional
    @Modifying
    @Query("DELETE FROM Annonce")
    void deleteAllAnnoncesForce();
}
