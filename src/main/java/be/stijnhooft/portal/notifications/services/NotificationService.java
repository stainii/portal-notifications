package be.stijnhooft.portal.notifications.services;

import be.stijnhooft.portal.notifications.model.Notification;
import be.stijnhooft.portal.notifications.repositories.NotificationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.List;

@Service
@Transactional
public class NotificationService {

  private final NotificationRepository notificationRepository;

  @Inject
  public NotificationService(NotificationRepository notificationRepository) {
    this.notificationRepository = notificationRepository;
  }

  public List<Notification> findByRead(boolean read) {
    return notificationRepository.findByRead(read);
  }

  public List<Notification> findAll() {
    return notificationRepository.findAll();
  }

}
