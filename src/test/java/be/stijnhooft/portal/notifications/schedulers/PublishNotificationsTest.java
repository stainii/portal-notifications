package be.stijnhooft.portal.notifications.schedulers;

import be.stijnhooft.portal.notifications.entities.NotificationEntity;
import be.stijnhooft.portal.notifications.mappers.NotificationMapper;
import be.stijnhooft.portal.notifications.messaging.NotificationPublisher;
import be.stijnhooft.portal.notifications.model.Notification;
import be.stijnhooft.portal.notifications.repositories.NotificationRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertTrue;
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
    private LocalDateTime nowAtZeroSeconds = LocalDateTime.of(2019, 5, 30, 12, 10, 0, 0);
    private LocalDateTime nowPlusOneMinuteAtZeroSeconds = LocalDateTime.of(2019, 5, 30, 12, 11, 0, 0);

    @Before
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

        //mock
        doReturn(notificationEntities).when(notificationRepository).findNotificationsThatShouldBePublishedBetween(nowAtZeroSeconds, nowPlusOneMinuteAtZeroSeconds);
        doReturn(notifications).when(notificationMapper).mapEntitiesToModel(notificationEntities);

        //execute
        publishNotifications.publishNotifications();

        //verify
        verify(notificationRepository).findNotificationsThatShouldBePublishedBetween(nowAtZeroSeconds, nowPlusOneMinuteAtZeroSeconds);
        verify(notificationMapper).mapEntitiesToModel(notificationEntities);
        verify(notificationPublisher).publish(notifications);
        verifyNoMoreInteractions(notificationRepository, notificationPublisher, notificationMapper);

        assertTrue(notificationEntity1.isPublished());
        assertTrue(notificationEntity2.isPublished());
    }

    @Test
    public void publishNotificationsWhenNoneExist() {
        //mock
        doReturn(new ArrayList<>()).when(notificationRepository).findNotificationsThatShouldBePublishedBetween(nowAtZeroSeconds, nowPlusOneMinuteAtZeroSeconds);

        //execute
        publishNotifications.publishNotifications();

        //verify
        verify(notificationRepository).findNotificationsThatShouldBePublishedBetween(nowAtZeroSeconds, nowPlusOneMinuteAtZeroSeconds);
        verifyNoMoreInteractions(notificationRepository, notificationPublisher, notificationMapper);
    }
}
