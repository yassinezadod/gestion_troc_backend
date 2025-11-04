package com.troc.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.troc.dto.NotificationRequest;
import com.troc.dto.NotificationUpdateRequest;
import com.troc.entity.Notification;
import com.troc.entity.User;
import com.troc.repository.NotificationRepository;
import com.troc.repository.UserRepository;
import com.troc.security.JwtUtil;
import com.troc.service.AnnonceService;
import com.troc.service.NotificationService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("api/notifications")
public class NotificationController {
	private final NotificationService notificationService;
	private final NotificationRepository notificationRepository;
	 private final UserRepository userRepository;
	private final JwtUtil jwtUtil;

	public NotificationController(NotificationService notificationService, NotificationRepository notificationRepository, UserRepository userRepository, JwtUtil jwtUtil) {
		this.notificationService = notificationService;
	     this.jwtUtil = jwtUtil;
	     this.userRepository = userRepository;
	     this.notificationRepository = notificationRepository;
	}
	
	
	
	//--cree une notification --
	
	@PostMapping
	public ResponseEntity<Map<String , String>> addNotification(@Valid @RequestBody NotificationRequest request ){
		try {
		notificationService.addNotification(request);
		 Map<String, String> response = new HashMap<>();
		 response.put("status", "success");
		  response.put("message", "Notification créée avec succès");
		  return ResponseEntity.status(HttpStatus.CREATED).body(response);	
			
		}catch(RuntimeException e) {
		Map<String, String> error = new HashMap<>();
	    error.put("status", "error");
	    error.put("message", e.getMessage());
	    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
			
		}
		
	}
	
	
	// -- voir toutes les notifications --
	
	@GetMapping
	public ResponseEntity<?> getAllNotifications(){
		try {
			return ResponseEntity.ok(notificationService.getAllNotifications());
			
		}catch(RuntimeException e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(Map.of(
						    "status", "error",
						    "message", e.getMessage() == null ? "Erreur inconnue" : e.getMessage()
						));

			
		}
	}
	
//---- PATCH /api/notifications/{id}/read-------
	
	@PatchMapping("/{id}/read")
	  public ResponseEntity<?> markNotificationAsRead(
	            @PathVariable UUID id,
	            @RequestBody(required = false) 
	            NotificationUpdateRequest request,
	            HttpServletRequest httpRequest) {
		
	      // Récupérer le token JWT depuis l'en-tête
        String authHeader = httpRequest.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body("Token manquant");
        }

        String token = authHeader.substring(7);
        
        
     // Valider le token
        if (!jwtUtil.validateToken(token)) {
            return ResponseEntity.status(401).body("Token invalide ou expiré");
        }

        // Extraire l'utilisateur à partir du token
        String email = jwtUtil.getEmailFromToken(token);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
		
        try {
        	Notification markNotificationAsRead = notificationService.markAsReadByUser(id, request, user.getId());
        	return ResponseEntity.ok(markNotificationAsRead);
        	
        } catch (RuntimeException e) {
        	  // Gestion des erreurs : pas autorisé ou annonce introuvable
            return ResponseEntity.status(403).body(e.getMessage());
        }
			
		
	}
	
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteNotificationById(@PathVariable UUID id) {
	    try {
	        notificationRepository.deleteById(id);
	        return ResponseEntity.ok(Map.of(
	            "status", "success",
	            "message", "Notification supprimée avec succès",
	            "deletedNotificationId", id
	        ));
	    } catch (Exception e) {
	        return ResponseEntity.status(400).body(Map.of(
	            "status", "error",
	            "message", e.getMessage()
	        ));
	    }
	}
	
	@DeleteMapping
	public ResponseEntity<?> deleteAllNotifications() {
	    try {
	        notificationRepository.deleteAll();
	        return ResponseEntity.ok(Map.of(
	            "status", "success",
	            "message", "Toutes les notifications ont été supprimées avec succès"
	        ));
	    } catch (Exception e) {
	        return ResponseEntity.status(400).body(Map.of(
	            "status", "error",
	            "message", e.getMessage()
	        ));
	    }
	}


	
	
	
}
