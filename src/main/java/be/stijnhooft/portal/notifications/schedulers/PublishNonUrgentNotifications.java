package be.stijnhooft.portal.notifications.schedulers;

import be.stijnhooft.portal.notifications.entities.Notification;
import be.stijnhooft.portal.notifications.model.NotificationUrgency;
import be.stijnhooft.portal.notifications.repositories.NotificationRepository;
import be.stijnhooft.portal.notifications.services.PublishService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class PublishNonUrgentNotifications {

  private final NotificationRepository notificationRepository;
  private final PublishService publishService;

  @Inject
  public PublishNonUrgentNotifications(NotificationRepository notificationRepository, PublishService publishService) {
    this.notificationRepository = notificationRepository;
    this.publishService = publishService;
  }

  @Scheduled(cron = "0   18  *   *   *  *")
  public void publishNonUrgentNotifications() {
    List<Notification> notifications = notificationRepository.findByUrgencyAndDateGreaterThanEqual(NotificationUrgency.PUBLISH_WITHIN_24_HOURS, LocalDateTime.now().minusDays(1));
    publishService.publishNotifications(notifications);
  }

}