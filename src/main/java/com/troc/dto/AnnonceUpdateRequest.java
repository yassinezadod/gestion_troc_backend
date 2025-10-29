package com.troc.dto;

import com.troc.entity.AnnonceState;

public class AnnonceUpdateRequest {

    private String title;
    private String description;
    private String desiredProduct;
    private Double price;
    private AnnonceState state;
    private String productId; // UUID du produit si changement

    // --- Getters & Setters ---
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

    public String getProductId() { return productId; }
    public void setProductId(String productId) { this.productId = productId; }
}
