package com.troc.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "users")
public class User {
    @Id
    @Column(columnDefinition = "UUID")
    private UUID id = UUID.randomUUID();

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    private String firstName;
    private String lastName;
    private String phoneCode;
    private Long phoneNumber;
    private String city;
    private String address;
    private String profilePicture;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime updatedAt = LocalDateTime.now();
    
    // Un utilisateur peut créer plusieurs produit
    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true)
    private java.util.List<Product> products = new java.util.ArrayList<>();
    
    // Un utilisateur peut créer plusieurs annonces
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Annonce> annonces = new ArrayList<>();
    
    // Un utilisateur recoit plusieur notification
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private java.util.List<Notification> notifications = new java.util.ArrayList<>();


    // getters & setters

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getPhoneCode() {
		return phoneCode;
	}

	public void setPhoneCode(String phoneCode) {
		this.phoneCode = phoneCode;
	}

	public Long getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(Long phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getProfilePicture() {
		return profilePicture;
	}

	public void setProfilePicture(String profilePicture) {
		this.profilePicture = profilePicture;
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
	
	
	 public List<Product> getProducts() {
	        return products;
	    }

	    public void setProducts(List<Product> products) {
	        this.products = products;
	    }

	    public List<Annonce> getAnnonces() {
	        return annonces;
	    }

	    public void setAnnonces(List<Annonce> annonces) {
	        this.annonces = annonces;
	    }
	    
	    
	    public List<Notification> getNotifications() {
	        return notifications;
	    }

	    public void setNotifications(List<Notification> notifications) {
	        this.notifications = notifications;
	    }


   
    
    
}
