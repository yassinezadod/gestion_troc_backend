package com.troc.service;

import com.troc.dto.ProductRequest;
import com.troc.entity.Product;
import com.troc.entity.ProductState;
import com.troc.entity.User;
import com.troc.repository.ProductRepository;

import jakarta.persistence.EntityNotFoundException;

import org.springframework.security.access.AccessDeniedException;

import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Service;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product addProduct(ProductRequest request, User owner) {
        Product product = new Product();
        product.setTitle(request.getTitle());
        product.setDescription(request.getDescription());
        product.setCategory(request.getCategory());
        product.setState(request.getState());
        product.setYearsUsed(request.getYearsUsed());
        product.setPicture(request.getPicture());
        product.setOwner(owner);
        return productRepository.save(product);
    }
    
    //  Modifier partiellement un produit (PATCH)
    public Product updateProductPartially(UUID id, Map<String, Object> updates, String userEmail) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));

        // Vérifier que l'utilisateur est bien le propriétaire
        if (!product.getOwner().getEmail().equals(userEmail)) {
        	 throw new RuntimeException("You are not allowed to update this product");
        }

        // Appliquer les mises à jour partielles
        updates.forEach((key, value) -> {
            switch (key) {
                case "title" -> product.setTitle((String) value);
                case "description" -> product.setDescription((String) value);
                case "category" -> product.setCategory((String) value);
                case "state" -> {
                    if (value instanceof String strValue) {
                        product.setState(ProductState.valueOf(strValue));
                    }
                }
                case "yearsUsed" -> product.setYearsUsed(Integer.valueOf(value.toString()));
                case "picture" -> product.setPicture((String) value);
            }
        });

        return productRepository.save(product);
    }

}
