package be.stijnhooft.portal.notifications.schedulers;

import be.stijnhooft.portal.notifications.entities.Notification;
import be.stijnhooft.portal.notifications.messaging.NotificationPublisher;
import be.stijnhooft.portal.notifications.model.NotificationUrgency;
import be.stijnhooft.portal.notifications.repositories.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class PublishNonUrgentNotifications {

  private final NotificationRepository notificationRepository;
  private final NotificationPublisher notificationPublisher;

  @Autowired
  public PublishNonUrgentNotifications(NotificationRepository notificationRepository, NotificationPublisher notificationPublisher) {
    this.notificationRepository = notificationRepository;
    this.notificationPublisher = notificationPublisher;
  }

  @Scheduled(cron = "0   18  *   *   *  *")
  public void publishNonUrgentNotifications() {
    List<Notification> notifications = notificationRepository.findByUrgencyAndDateGreaterThanEqual(NotificationUrgency.PUBLISH_WITHIN_24_HOURS, LocalDateTime.now().minusDays(1));
    if (!notifications.isEmpty()) {
        notificationPublisher.publish(notifications);
    }
  }

}
