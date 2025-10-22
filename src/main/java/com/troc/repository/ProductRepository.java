package com.troc.repository;

import com.troc.entity.Product;
import com.troc.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {

    //  Trouver tous les produits d’un utilisateur spécifique
    List<Product> findByOwner(User owner);

    //  Rechercher par catégorie
    List<Product> findByCategory(String category);

    // Rechercher par état (New / Used)
    List<Product> findByState(String state);

    //  Rechercher par titre (insensible à la casse)
    List<Product> findByTitleContainingIgnoreCase(String title);
}
