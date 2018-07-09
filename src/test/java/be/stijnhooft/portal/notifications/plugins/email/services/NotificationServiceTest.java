package be.stijnhooft.portal.notifications.plugins.email.services;

import be.stijnhooft.portal.notifications.entities.Notification;
import be.stijnhooft.portal.notifications.entities.NotificationAction;
import be.stijnhooft.portal.notifications.messaging.NotificationPublisher;
import be.stijnhooft.portal.notifications.model.NotificationUrgency;
import be.stijnhooft.portal.notifications.repositories.NotificationRepository;
import be.stijnhooft.portal.notifications.services.NotificationService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@RunWith(SpringRunner.class)
public class NotificationServiceTest {

    @InjectMocks
    private NotificationService notificationService;

    @Mock
    private NotificationRepository notificationRepository;

    @Mock
    private NotificationPublisher notificationPublisher;

    @Test
    public void saveAndIfUrgentThenPublish() {
        // data set
        Notification urgentNotification = new Notification(null, "Housagotchi",
            LocalDateTime.now(), "urgent notification", "hurry up!",
            new NotificationAction("url", "text"),
            NotificationUrgency.PUBLISH_IMMEDIATELY, false);
        Notification nonUrgentNotification = new Notification(null, "Housagotchi",
            LocalDateTime.now(), "non-urgent notification", "chill...",
            new NotificationAction("url", "text"),
            NotificationUrgency.PUBLISH_WITHIN_24_HOURS, false);
        List<Notification> notifications = Arrays.asList(urgentNotification, nonUrgentNotification);

        // execute
        notificationService.saveAndIfUrgentThenPublish(notifications);

        // verify and assert
        notifications.forEach(notification -> verify(notificationRepository).save(notification));
        verify(notificationPublisher).publish(Arrays.asList(urgentNotification));
        verifyNoMoreInteractions(notificationRepository, notificationPublisher);
    }
}
