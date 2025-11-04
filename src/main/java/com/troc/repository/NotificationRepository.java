package com.troc.repository;

import com.troc.entity.Notification;
import com.troc.entity.User;
import com.troc.entity.NotificationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, UUID> {

    //  Récupérer toutes les notifications d’un utilisateur
    List<Notification> findByUser(User user);

    //  Récupérer toutes les notifications non lues d’un utilisateur
    List<Notification> findByUserAndIsReadFalse(User user);

    //  Récupérer toutes les notifications par type (INFO, ALERT, REMINDER)
    List<Notification> findByType(NotificationType type);

    //  Supprimer toutes les notifications d’un utilisateur
    void deleteByUser(User user);
    
    
}
