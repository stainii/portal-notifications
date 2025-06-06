package be.stijnhooft.portal.notifications.services;

import be.stijnhooft.portal.model.domain.Event;
import be.stijnhooft.portal.model.domain.FlowAction;
import be.stijnhooft.portal.notifications.entities.NotificationEntity;
import be.stijnhooft.portal.notifications.exceptions.NotificationNotFoundException;
import be.stijnhooft.portal.notifications.mappers.NotificationMapper;
import be.stijnhooft.portal.notifications.messaging.NotificationPublisher;
import be.stijnhooft.portal.notifications.repositories.NotificationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
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

    @Test
    public void markAsReadWhenFailureBecauseIdIsNull() {
        assertThrows(NullPointerException.class, () ->
            // execute
            notificationService.markAsRead(null, true));
    }

    @Test
    public void markAsReadWhenFailureBecauseNotificationDoesNotExist() {
        assertThrows(NotificationNotFoundException.class, () -> {
            // data set
            Long id = 2L;

            // mock
            doReturn(Optional.empty()).when(notificationRepository).findById(id);

            // execute
            notificationService.markAsRead(id, true);
        });
    }

    @Test
    public void cancelNotifications() {
        // data set
        Event event1 = new Event("Housagotchi", "abc", FlowAction.END, LocalDateTime.now().minusHours(1), new HashMap<>());
        Event event2 = new Event("Housagotchi", "def", FlowAction.END, LocalDateTime.now().minusHours(2), new HashMap<>());
        List<Event> events = Arrays.asList(event1, event2);

        // execute
        notificationService.cancelNotifications(events);

        // verify and assert
        verify(notificationRepository).cancelNotificationsWithFlowIdAndBefore(event1.getFlowId(), event1.getPublishDate());
        verify(notificationRepository).cancelNotificationsWithFlowIdAndBefore(event2.getFlowId(), event2.getPublishDate());
        verifyNoMoreInteractions(notificationMapper, notificationPublisher, notificationRepository);
    }
}
