package be.stijnhooft.portal.notifications.schedulers;

import be.stijnhooft.portal.model.notification.Notification;
import be.stijnhooft.portal.notifications.entities.NotificationEntity;
import be.stijnhooft.portal.notifications.mappers.NotificationMapper;
import be.stijnhooft.portal.notifications.messaging.NotificationPublisher;
import be.stijnhooft.portal.notifications.repositories.NotificationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class PublishNotificationsTest {

    private PublishNotifications publishNotifications;

    @Mock
    private NotificationRepository notificationRepository;

    @Mock
    private NotificationPublisher notificationPublisher;

    @Mock
    private NotificationMapper notificationMapper;

    private Clock clock = Clock.fixed(ZonedDateTime.of(2019, 5, 30, 12, 10, 0, 0, ZoneId.systemDefault()).toInstant(), ZoneId.systemDefault());
    private LocalDateTime now = LocalDateTime.ofInstant(clock.instant(), ZoneId.systemDefault());

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
        publishNotifications = new PublishNotifications(notificationRepository, notificationPublisher, notificationMapper, clock);
    }

    @Test
    public void publishNotificationsWhenTheyExist() {
        //data set
        NotificationEntity notificationEntity1 = new NotificationEntity();
        NotificationEntity notificationEntity2 = new NotificationEntity();

        List<NotificationEntity> notificationEntities = Arrays.asList(notificationEntity1, notificationEntity2);
        List<Notification> notifications = Arrays.asList(new Notification(), new Notification());

        LocalDateTime lastPublishDate = LocalDateTime.of(2019, 5, 30, 12, 9, 2);

        //mock
        doReturn(lastPublishDate).when(notificationRepository).findLastPublishDate();
        doReturn(notificationEntities).when(notificationRepository).findNotificationsThatShouldBePublishedBetween(lastPublishDate, now);
        doReturn(notifications).when(notificationMapper).mapEntitiesToModel(notificationEntities);

        //execute
        publishNotifications.publishNotifications();

        //verify
        verify(notificationRepository).findLastPublishDate();
        verify(notificationRepository).findNotificationsThatShouldBePublishedBetween(lastPublishDate, now);
        verify(notificationMapper).mapEntitiesToModel(notificationEntities);
        verify(notificationPublisher).publish(notifications);
        verifyNoMoreInteractions(notificationRepository, notificationPublisher, notificationMapper);

        assertTrue(notificationEntity1.isPublished());
        assertTrue(notificationEntity2.isPublished());
    }

    @Test
    public void publishNotificationsWhenNoneExist() {
        // data set
        LocalDateTime lastPublishDate = LocalDateTime.of(2019, 5, 30, 12, 9, 2);

        //mock
        doReturn(lastPublishDate).when(notificationRepository).findLastPublishDate();
        doReturn(new ArrayList<>()).when(notificationRepository).findNotificationsThatShouldBePublishedBetween(lastPublishDate, now);

        //execute
        publishNotifications.publishNotifications();

        //verify
        verify(notificationRepository).findLastPublishDate();
        verify(notificationRepository).findNotificationsThatShouldBePublishedBetween(lastPublishDate, now);
        verifyNoMoreInteractions(notificationRepository, notificationPublisher, notificationMapper);
    }

    @Test
    public void publishNotificationsWhenItsTheFirstTimeEverNotificationsAreBeingPublished() {
        //data set
        NotificationEntity notificationEntity1 = new NotificationEntity();
        NotificationEntity notificationEntity2 = new NotificationEntity();

        List<NotificationEntity> notificationEntities = Arrays.asList(notificationEntity1, notificationEntity2);
        List<Notification> notifications = Arrays.asList(new Notification(), new Notification());

        LocalDateTime minDate = LocalDateTime.of(2018, 1, 1, 0, 0);

        //mock
        doReturn(null).when(notificationRepository).findLastPublishDate();
        doReturn(notificationEntities).when(notificationRepository).findNotificationsThatShouldBePublishedBetween(minDate, now);
        doReturn(notifications).when(notificationMapper).mapEntitiesToModel(notificationEntities);

        //execute
        publishNotifications.publishNotifications();

        //verify
        verify(notificationRepository).findLastPublishDate();
        verify(notificationRepository).findNotificationsThatShouldBePublishedBetween(minDate, now);
        verify(notificationMapper).mapEntitiesToModel(notificationEntities);
        verify(notificationPublisher).publish(notifications);
        verifyNoMoreInteractions(notificationRepository, notificationPublisher, notificationMapper);

        assertTrue(notificationEntity1.isPublished());
        assertTrue(notificationEntity2.isPublished());
    }
}
