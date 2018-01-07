package be.stijnhooft.portal.notifications.repositories;

import be.stijnhooft.portal.notifications.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

  List<Notification> findByRead(boolean read);

}
