package be.stijnhooft.portal.notifications.services;

import be.stijnhooft.portal.model.domain.Event;
import be.stijnhooft.portal.notifications.entities.NotificationActionEmbeddable;
import be.stijnhooft.portal.notifications.entities.NotificationEntity;
import be.stijnhooft.portal.notifications.exceptions.NotificationNotFoundException;
import be.stijnhooft.portal.notifications.mappers.NotificationMapper;
import be.stijnhooft.portal.notifications.messaging.NotificationPublisher;
import be.stijnhooft.portal.notifications.model.Notification;
import be.stijnhooft.portal.notifications.model.NotificationAction;
import be.stijnhooft.portal.notifications.model.PublishStrategy;
import be.stijnhooft.portal.notifications.repositories.NotificationRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
public class NotificationServiceTest {

    @InjectMocks
    private NotificationService notificationService;

    @Mock
    private NotificationRepository notificationRepository;

    @Mock
    private NotificationPublisher notificationPublisher;

    @Mock
    private NotificationMapper notificationMapper;

    @Test
    public void saveAndIfUrgentThenPublish() {
        // data set
        NotificationEntity urgentNotificationEntity = new NotificationEntity("Housagotchi",
            "flow1", "urgent notification", "hurry up!",
            new NotificationActionEmbeddable("url", "text"),
            PublishStrategy.PUBLISH_IMMEDIATELY, LocalDateTime.now(), LocalDateTime.now());
        NotificationEntity nonUrgentNotificationEntity = new NotificationEntity("Housagotchi",
            "flow2", "non-urgent notification", "chill...",
            new NotificationActionEmbeddable("url", "text"),
            PublishStrategy.PUBLISH_AT_SPECIFIC_DATE_TIME, LocalDateTime.now(), LocalDateTime.now());
        List<NotificationEntity> notificationEntities = Arrays.asList(urgentNotificationEntity, nonUrgentNotificationEntity);

        Notification urgentNotification = new Notification(1L, "Housagotchi",
            LocalDateTime.now(), "urgent notification", "hurry up!",
            new NotificationAction("url", "text", "internalUrl"),
            PublishStrategy.PUBLISH_IMMEDIATELY);
        Notification nonUrgentNotification = new Notification(2L, "Housagotchi",
            LocalDateTime.now(), "non-urgent notification", "chill...",
            new NotificationAction("url", "text", "internalUrl"),
            PublishStrategy.PUBLISH_AT_SPECIFIC_DATE_TIME);
        List<Notification> notifications = Arrays.asList(urgentNotification, nonUrgentNotification);

        // mock
        doReturn(notifications).when(notificationMapper).mapEntitiesToModel(notificationEntities);

        // execute
        notificationService.saveAndIfUrgentThenPublish(notificationEntities);

        // verify and assert
        notificationEntities.forEach(notificationEntity -> verify(notificationRepository).save(notificationEntity));
        verify(notificationRepository).flush();
        verify(notificationMapper).mapEntitiesToModel(notificationEntities);
        verify(notificationPublisher).publish(Arrays.asList(urgentNotification));
        verifyNoMoreInteractions(notificationRepository, notificationPublisher, notificationMapper);
    }

    @Test
    public void markAsReadWhenSuccessAndReadIsUpdatedFromFalseToTrue() {
        // data set
        NotificationEntity notification = new NotificationEntity();
        notification.setRead(false);
        Long id = 1L;

        // mock
        doReturn(Optional.of(notification)).when(notificationRepository).findById(id);

        // execute
        notificationService.markAsRead(id, true);

        // verify and assert
        verify(notificationRepository).findById(id);
        verifyNoMoreInteractions(notificationRepository);

        assertTrue(notification.isRead());
    }

    @Test
    public void markAsReadWhenSuccessAndReadIsUpdatedFromFalseToFalse() {
        // data set
        NotificationEntity notification = new NotificationEntity();
        notification.setRead(false);
        Long id = 1L;

        // mock
        doReturn(Optional.of(notification)).when(notificationRepository).findById(id);

        // execute
        notificationService.markAsRead(id, false);

        // verify and assert
        verify(notificationRepository).findById(id);
        verifyNoMoreInteractions(notificationRepository);

        assertFalse(notification.isRead());
    }

    @Test
    public void markAsReadWhenSuccessAndReadIsUpdatedFromTrueToFalse() {
        // data set
        NotificationEntity notification = new NotificationEntity();
        notification.setRead(true);
        Long id = 1L;

        // mock
        doReturn(Optional.of(notification)).when(notificationRepository).findById(id);

        // execute
        notificationService.markAsRead(id, false);

        // verify and assert
        verify(notificationRepository).findById(id);
        verifyNoMoreInteractions(notificationRepository);

        assertFalse(notification.isRead());
    }

    @Test
    public void markAsReadWhenSuccessAndReadIsUpdatedFromTrueToTrue() {
        // data set
        NotificationEntity notification = new NotificationEntity();
        notification.setRead(true);
        Long id = 1L;

        // mock
        doReturn(Optional.of(notification)).when(notificationRepository).findById(id);

        // execute
        notificationService.markAsRead(id, true);

        // verify and assert
        verify(notificationRepository).findById(id);
        verifyNoMoreInteractions(notificationRepository);

        assertTrue(notification.isRead());
    }

    @Test(expected = NullPointerException.class)
    public void markAsReadWhenFailureBecauseIdIsNull() {
        // execute
        notificationService.markAsRead(null, true);
    }

    @Test(expected = NotificationNotFoundException.class)
    public void markAsReadWhenFailureBecauseNotificationDoesNotExist() {
        // data set
        Long id = 2L;

        // mock
        doReturn(Optional.empty()).when(notificationRepository).findById(id);

        // execute
        notificationService.markAsRead(id, true);
    }

    @Test
    public void cancelNotifications() {
        // data set
        Event event1 = new Event("Housagotchi", "abc", LocalDateTime.now().minusHours(1), new HashMap<>());
        Event event2 = new Event("Housagotchi", "def", LocalDateTime.now().minusHours(2), new HashMap<>());
        List<Event> events = Arrays.asList(event1, event2);

        // execute
        notificationService.cancelNotifications(events);

        // verify and assert
        verify(notificationRepository).cancelNotificationsWithFlowIdAndBefore(event1.getFlowId(), event1.getPublishDate());
        verify(notificationRepository).cancelNotificationsWithFlowIdAndBefore(event2.getFlowId(), event2.getPublishDate());
        verifyNoMoreInteractions(notificationMapper, notificationPublisher, notificationRepository);
    }
}
