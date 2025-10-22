package com.troc.dto;

import com.troc.entity.ProductState;
import java.time.LocalDateTime;
import java.util.UUID;

public class ProductResponse {
    private UUID id;
    private String title;
    private String description;
    private String category;
    private ProductState state;
    private Integer yearsUsed;
    private String picture;

    // Infos du propriétaire
    private UUID ownerId;
    private String ownerEmail;
    private String ownerFirstName;
    private String ownerLastName;
    private String ownerPhoneCode;
    private Long ownerPhoneNumber;
    private String ownerCity;
    private String ownerAddress;
    private String ownerProfilePicture;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public ProductResponse(UUID id, String title, String description, String category,
                           ProductState state, Integer yearsUsed, String picture,
                           UUID ownerId, String ownerEmail, String ownerFirstName,
                           String ownerLastName, String ownerPhoneCode, Long ownerPhoneNumber,
                           String ownerCity, String ownerAddress, String ownerProfilePicture,
                           LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.category = category;
        this.state = state;
        this.yearsUsed = yearsUsed;
        this.picture = picture;
        this.ownerId = ownerId;
        this.ownerEmail = ownerEmail;
        this.ownerFirstName = ownerFirstName;
        this.ownerLastName = ownerLastName;
        this.ownerPhoneCode = ownerPhoneCode;
        this.ownerPhoneNumber = ownerPhoneNumber;
        this.ownerCity = ownerCity;
        this.ownerAddress = ownerAddress;
        this.ownerProfilePicture = ownerProfilePicture;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public ProductState getState() {
		return state;
	}

	public void setState(ProductState state) {
		this.state = state;
	}

	public Integer getYearsUsed() {
		return yearsUsed;
	}

	public void setYearsUsed(Integer yearsUsed) {
		this.yearsUsed = yearsUsed;
	}

	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}

	public UUID getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(UUID ownerId) {
		this.ownerId = ownerId;
	}

	public String getOwnerEmail() {
		return ownerEmail;
	}

	public void setOwnerEmail(String ownerEmail) {
		this.ownerEmail = ownerEmail;
	}

	public String getOwnerFirstName() {
		return ownerFirstName;
	}

	public void setOwnerFirstName(String ownerFirstName) {
		this.ownerFirstName = ownerFirstName;
	}

	public String getOwnerLastName() {
		return ownerLastName;
	}

	public void setOwnerLastName(String ownerLastName) {
		this.ownerLastName = ownerLastName;
	}

	public String getOwnerPhoneCode() {
		return ownerPhoneCode;
	}

	public void setOwnerPhoneCode(String ownerPhoneCode) {
		this.ownerPhoneCode = ownerPhoneCode;
	}

	public Long getOwnerPhoneNumber() {
		return ownerPhoneNumber;
	}

	public void setOwnerPhoneNumber(Long ownerPhoneNumber) {
		this.ownerPhoneNumber = ownerPhoneNumber;
	}

	public String getOwnerCity() {
		return ownerCity;
	}

	public void setOwnerCity(String ownerCity) {
		this.ownerCity = ownerCity;
	}

	public String getOwnerAddress() {
		return ownerAddress;
	}

	public void setOwnerAddress(String ownerAddress) {
		this.ownerAddress = ownerAddress;
	}

	public String getOwnerProfilePicture() {
		return ownerProfilePicture;
	}

	public void setOwnerProfilePicture(String ownerProfilePicture) {
		this.ownerProfilePicture = ownerProfilePicture;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}

    // Getters et setters
    
    
    
}
