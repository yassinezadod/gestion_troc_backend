package com.troc.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public class UserResponse {
    private UUID id;
    private String email;
    private String firstName;
    private String lastName;
    private String phoneCode;
    private Long phoneNumber; // doit rester Long pour correspondre à User
    private String city;
    private String address;
    private String profilePicture;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Constructeur complet
    public UserResponse(UUID id, String email, String firstName, String lastName,
                        String phoneCode, Long phoneNumber, String city,
                        String address, String profilePicture,
                        LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneCode = phoneCode;
        this.phoneNumber = phoneNumber;
        this.city = city;
        this.address = address;
        this.profilePicture = profilePicture;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters et setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getPhoneCode() { return phoneCode; }
    public void setPhoneCode(String phoneCode) { this.phoneCode = phoneCode; }

    public Long getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(Long phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getProfilePicture() { return profilePicture; }
    public void setProfilePicture(String profilePicture) { this.profilePicture = profilePicture; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
