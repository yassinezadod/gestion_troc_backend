package com.troc.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class RegisterRequest {
	@NotBlank
	@Email(message = "Email invalide")
	@jakarta.validation.constraints.Pattern(
		    regexp = "^[A-Za-z0-9._%+-]+@(gmail\\.com|outlook\\.com|yahoo\\.fr|hotmail\\.com)$",
		    message = "Email doit être un Gmail, Outlook, Yahoo ou Hotmail valide"
		)
    private String email;

    @NotBlank @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;
    
    
    // getters & setters

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

 
    
    

}
