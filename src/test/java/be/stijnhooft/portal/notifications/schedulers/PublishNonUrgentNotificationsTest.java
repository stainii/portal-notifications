package be.stijnhooft.portal.notifications.schedulers;

import be.stijnhooft.portal.notifications.entities.Notification;
import be.stijnhooft.portal.notifications.messaging.NotificationPublisher;
import be.stijnhooft.portal.notifications.model.NotificationUrgency;
import be.stijnhooft.portal.notifications.repositories.NotificationRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
public class PublishNonUrgentNotificationsTest {

    @InjectMocks
    private PublishNonUrgentNotifications publishNonUrgentNotifications;

    @Mock
    private NotificationRepository notificationRepository;

    @Mock
    private NotificationPublisher notificationPublisher;

    @Test
    public void publishNonUrgentNotificationsWhenTheyExist() {
        //data set
        List<Notification> notifications = Arrays.asList(new Notification());

        //mock
        doReturn(notifications).when(notificationRepository).findByUrgencyAndDateGreaterThanEqual(eq(NotificationUrgency.PUBLISH_WITHIN_24_HOURS), isA(LocalDateTime.class));

        //execute
        publishNonUrgentNotifications.publishNonUrgentNotifications();

        //verify
        verify(notificationRepository).findByUrgencyAndDateGreaterThanEqual(eq(NotificationUrgency.PUBLISH_WITHIN_24_HOURS), isA(LocalDateTime.class));
        verify(notificationPublisher).publish(notifications);
        verifyNoMoreInteractions(notificationRepository, notificationPublisher);
    }

    @Test
    public void publishNonUrgentNotificationsWhenNoneExist() {
        //mock
        doReturn(new ArrayList<>()).when(notificationRepository).findByUrgencyAndDateGreaterThanEqual(eq(NotificationUrgency.PUBLISH_WITHIN_24_HOURS), isA(LocalDateTime.class));

        //execute
        publishNonUrgentNotifications.publishNonUrgentNotifications();

        //verify
        verify(notificationRepository).findByUrgencyAndDateGreaterThanEqual(eq(NotificationUrgency.PUBLISH_WITHIN_24_HOURS), isA(LocalDateTime.class));
        verifyNoMoreInteractions(notificationRepository, notificationPublisher);
    }
}
