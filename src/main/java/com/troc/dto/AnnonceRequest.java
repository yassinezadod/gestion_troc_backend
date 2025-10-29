package com.troc.dto;

import java.util.UUID;
import com.troc.entity.AnnonceState;

public class AnnonceRequest {
    private String title;
    private String description;
    private String desiredProduct;
    private Double price;
    private UUID productId;
    private AnnonceState state; 

    // --- Getters & Setters ---
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getDesiredProduct() { return desiredProduct; }
    public void setDesiredProduct(String desiredProduct) { this.desiredProduct = desiredProduct; }

    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }

    public UUID getProductId() { return productId; }
    public void setProductId(UUID productId) { this.productId = productId; }

    public AnnonceState getState() { return state; }
    public void setState(AnnonceState state) { this.state = state; }
}
