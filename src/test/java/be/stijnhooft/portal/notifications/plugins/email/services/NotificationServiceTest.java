package be.stijnhooft.portal.notifications.plugins.email.services;

import be.stijnhooft.portal.notifications.entities.Notification;
import be.stijnhooft.portal.notifications.entities.NotificationAction;
import be.stijnhooft.portal.notifications.model.NotificationUrgency;
import be.stijnhooft.portal.notifications.repositories.NotificationRepository;
import be.stijnhooft.portal.notifications.services.NotificationService;
import be.stijnhooft.portal.notifications.services.PublishService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.verify;

@RunWith(SpringRunner.class)
public class NotificationServiceTest {

  @InjectMocks
  private NotificationService notificationService;

  @Mock
  private NotificationRepository notificationRepository;

  @Mock
  private PublishService publishService;

  @Test
  public void saveAndIfUrgentThenPublish() {
    // data set
    Notification urgentNotification = new Notification(null, "Housagotchi",
      LocalDateTime.now(), "urgent notification", "hurry up!",
      new NotificationAction("url", "text"),
      NotificationUrgency.PUBLISH_IMMEDIATELY,false);
    Notification nonUrgentNotification = new Notification(null,"Housagotchi",
      LocalDateTime.now(), "non-urgent notification","chill...",
      new NotificationAction("url", "text"),
      NotificationUrgency.PUBLISH_WITHIN_24_HOURS, false);
    List<Notification> notifications = Arrays.asList(urgentNotification, nonUrgentNotification);

    // execute
    notificationService.saveAndIfUrgentThenPublish(notifications);

    // verify and assert
    verify(notificationRepository).save(notifications);
    verify(publishService).publishNotifications(Arrays.asList(urgentNotification));
  }
}
