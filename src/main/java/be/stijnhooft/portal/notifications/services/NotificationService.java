package be.stijnhooft.portal.notifications.services;

import be.stijnhooft.portal.notifications.entities.Notification;
import be.stijnhooft.portal.notifications.model.NotificationUrgency;
import be.stijnhooft.portal.notifications.repositories.NotificationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class NotificationService {

  private final NotificationRepository notificationRepository;
  private final PublishService publishService;

  @Inject
  public NotificationService(NotificationRepository notificationRepository, PublishService publishService) {
    this.notificationRepository = notificationRepository;
    this.publishService = publishService;
  }

  public List<Notification> findByRead(boolean read) {
    return notificationRepository.findByRead(read);
  }

  public List<Notification> findAll() {
    return notificationRepository.findAll();
  }

  //TODO: test
  public Collection<Notification> saveAndIfUrgentThenPublish(Collection<Notification> notifications) {
    notificationRepository.save(notifications);
    publishUrgentNotifications(notifications);
    //the other, non-urgent notifications, will be published by a scheduled method

    return notifications;
  }

  private void publishUrgentNotifications(Collection<Notification> notifications) {
    List<Notification> urgentNotifications = notifications.stream()
      .filter(notification -> notification.getUrgency() == NotificationUrgency.PUBLISH_IMMEDIATELY)
      .collect(Collectors.toList());
    publishService.publishNotifications(urgentNotifications);
  }
}
