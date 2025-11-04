package com.troc.dto;

import com.troc.entity.NotificationType;

public class NotificationUpdateRequest {
	
	private String content;
	private NotificationType type;
	private boolean isRead;
	
    // --- Getters & Setters ---
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public NotificationType getType() {
		return type;
	}
	public void setType(NotificationType type) {
		this.type = type;
	}
	public boolean isRead() {
		return isRead;
	}
	public void setRead(boolean isRead) {
		this.isRead = isRead;
	}

	
}
