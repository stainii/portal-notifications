package be.stijnhooft.portal.notifications.repositories;

import be.stijnhooft.portal.notifications.entities.Notification;
import be.stijnhooft.portal.notifications.model.NotificationUrgency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

  List<Notification> findByRead(boolean read);
  List<Notification> findByUrgencyAndDateGreaterThanEqual(NotificationUrgency urgency, LocalDateTime dateTime);

}
