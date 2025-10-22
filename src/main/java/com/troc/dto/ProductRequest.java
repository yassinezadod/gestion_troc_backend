package com.troc.dto;

import com.troc.entity.ProductState;

public class ProductRequest {
	 private String title;
	    private String description;
	    private String category;
	    private ProductState state;
	    private Integer yearsUsed;
	    private String picture;
	    
	 // Getters & Setters
	    public String getTitle() { return title; }
	    public void setTitle(String title) { this.title = title; }

	    public String getDescription() { return description; }
	    public void setDescription(String description) { this.description = description; }

	    public String getCategory() { return category; }
	    public void setCategory(String category) { this.category = category; }

	    public ProductState getState() { return state; }
	    public void setState(ProductState state) { this.state = state; }

	    public Integer getYearsUsed() { return yearsUsed; }
	    public void setYearsUsed(Integer yearsUsed) { this.yearsUsed = yearsUsed; }

	    public String getPicture() { return picture; }
	    public void setPicture(String picture) { this.picture = picture; }

}
