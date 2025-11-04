package com.troc.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.troc.dto.NotificationRequest;
import com.troc.dto.NotificationUpdateRequest;
import com.troc.entity.Notification;
import com.troc.entity.User;
import com.troc.repository.NotificationRepository;
import com.troc.repository.ProductRepository;
import com.troc.repository.UserRepository;

@Service
public class NotificationService {
	private final NotificationRepository notificationRepository;
	private final UserRepository userRepository;

	public NotificationService(NotificationRepository notificationRepository, UserRepository userRepository) {
		this.notificationRepository = notificationRepository;
		this.userRepository = userRepository;
		
		
	}
	
	
	public Notification addNotification(NotificationRequest request) {
		
		// -- vérifier que le utilisateur existe --
		User user = userRepository.findById(request.getUserID())
				.orElseThrow(()->new RuntimeException("Utilisateur non trouvé"));
		
		// -- Creer notification --
		Notification notification = new Notification();
		notification.setContent(request.getContent());
		notification.setType(request.getType());
		notification.setRead(request.isRead());
		notification.setUser(user);
		
		
		return notificationRepository.save(notification);
		
	}
	
	
	//-- Récupérer toutes les notifications --
	
	public List<Map<String, Object>> getAllNotifications() {
	    List<Notification> notifications = notificationRepository.findAll();

	    return notifications.stream().map(a -> {
	        Map<String, Object> userMap = new HashMap<>();
	        userMap.put("id", a.getUser().getId());
	        userMap.put("firstName", a.getUser().getFirstName());
	        userMap.put("lastName", a.getUser().getLastName());
	        userMap.put("Email", a.getUser().getEmail());
	        userMap.put("City", a.getUser().getCity());
	        userMap.put("address", a.getUser().getAddress());
	        userMap.put("phoneCode", a.getUser().getPhoneCode());
	        userMap.put("phoneNumber", a.getUser().getPhoneNumber());
	        userMap.put("profilePicture", a.getUser().getProfilePicture());
	        

	        Map<String, Object> notifMap = new HashMap<>();
	        notifMap.put("id", a.getId());
	        notifMap.put("content", a.getContent());
	        notifMap.put("type", a.getType());
	        notifMap.put("isRead", a.isRead());
	        notifMap.put("createdAt", a.getCreatedAt());
	        notifMap.put("user", userMap);

	        return notifMap;
	    }).collect(Collectors.toList());
	}
	
	public Notification markAsReadByUser(UUID notificationID, NotificationUpdateRequest request, UUID userID) {
		Notification notification = notificationRepository.findById(notificationID)
		  .orElseThrow(() -> new RuntimeException("Notification introuvable "));
		
		 // Vérifier que l'utilisateur connecté est bien le propriétaire
		if(!notification.getUser().getId().equals(userID)) {
			 throw new RuntimeException("Vous n'êtes pas autorisé à modifier cette notification ");
		}
		
		 // Mise à jour partielle des champs
		notification.setRead(true);
		
		return notificationRepository.save(notification);
	}


	
	

}
