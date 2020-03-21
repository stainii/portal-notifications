package be.stijnhooft.portal.notifications.mappers.publish_strategies;

import be.stijnhooft.portal.model.domain.Event;
import be.stijnhooft.portal.model.domain.FlowAction;
import be.stijnhooft.portal.notifications.dtos.FiringSubscription;
import be.stijnhooft.portal.notifications.entities.NotificationEntity;
import be.stijnhooft.portal.notifications.entities.SubscriptionEntity;
import be.stijnhooft.portal.notifications.entities.SubscriptionMappingToNotificationEmbeddable;
import be.stijnhooft.portal.notifications.repositories.NotificationRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import static be.stijnhooft.portal.notifications.model.PublishStrategy.PUBLISH_WITHIN_24_HOURS;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class PublishWithin24HoursPublishStrategyTest {

    private PublishWithin24HoursPublishStrategy strategy;

    @Mock
    private NotificationRepository notificationRepository;

    @Test
    public void applyWhenAnotherNotificationWillBePublishedWithin24Hours() {
        //data set
        SubscriptionMappingToNotificationEmbeddable mapping = new SubscriptionMappingToNotificationEmbeddable("", "", "", "", "");
        SubscriptionEntity subscription = new SubscriptionEntity(1L, "Housagotchi", "true", "false", mapping, PUBLISH_WITHIN_24_HOURS);
        HashMap<String, String> data = new HashMap<>();
        LocalDateTime publishDate = LocalDateTime.now();
        Event event = new Event("Housagotchi", "flowId", FlowAction.START, publishDate, data);

        LocalDateTime expected = LocalDateTime.of(2019, 5, 30, 14, 0);
        LocalDateTime now = LocalDateTime.of(2019, 5, 30, 12, 10, 12);
        LocalDateTime tomorrow = LocalDateTime.of(2019, 5, 31, 12, 10, 12);
        Clock clock = Clock.fixed(ZonedDateTime.of(2019, 5, 30, 12, 10, 12, 0, ZoneId.systemDefault()).toInstant(), ZoneId.systemDefault());

        NotificationEntity otherNotification = new NotificationEntity();
        otherNotification.setScheduledAt(expected);

        strategy = new PublishWithin24HoursPublishStrategy(notificationRepository, clock);

        // mock
        doReturn(Arrays.asList(otherNotification)).when(notificationRepository).findNotificationsThatShouldBePublishedBetween(now, tomorrow);

        //execute
        LocalDateTime result = strategy.apply(new FiringSubscription(subscription, event));

        //assert
        verify(notificationRepository).findNotificationsThatShouldBePublishedBetween(now, tomorrow);
        verifyNoMoreInteractions(notificationRepository);

        assertEquals(expected, result);
    }

    @Test
    public void applyWhenNoOtherNotificationWillBePublishedWithin24HoursAndCurrentTimeIsBefore1600() {
        //data set
        SubscriptionMappingToNotificationEmbeddable mapping = new SubscriptionMappingToNotificationEmbeddable("", "", "", "", "");
        SubscriptionEntity subscription = new SubscriptionEntity(1L, "Housagotchi", "true", "false", mapping, PUBLISH_WITHIN_24_HOURS);
        HashMap<String, String> data = new HashMap<>();

        LocalDateTime publishDate = LocalDateTime.now();
        LocalDateTime now = LocalDateTime.of(2019, 5, 30, 12, 10, 12);
        LocalDateTime tomorrow = LocalDateTime.of(2019, 5, 31, 12, 10, 12);
        Clock clock = Clock.fixed(ZonedDateTime.of(2019, 5, 30, 12, 10, 12, 0, ZoneId.systemDefault()).toInstant(), ZoneId.systemDefault());

        Event event = new Event("Housagotchi", "flowId", FlowAction.START, publishDate, data);

        strategy = new PublishWithin24HoursPublishStrategy(notificationRepository, clock);

        // mock
        doReturn(new ArrayList<>()).when(notificationRepository).findNotificationsThatShouldBePublishedBetween(now, tomorrow);

        //execute
        LocalDateTime result = strategy.apply(new FiringSubscription(subscription, event));

        //assert
        verify(notificationRepository).findNotificationsThatShouldBePublishedBetween(now, tomorrow);
        verifyNoMoreInteractions(notificationRepository);

        assertEquals(now.withHour(16).withMinute(0).withSecond(0), result);
    }

    @Test
    public void applyWhenNoOtherNotificationWillBePublishedWithin24HoursAndCurrentTimeIsAfter1600() {
        //data set
        SubscriptionMappingToNotificationEmbeddable mapping = new SubscriptionMappingToNotificationEmbeddable("", "", "", "", "");
        SubscriptionEntity subscription = new SubscriptionEntity(1L, "Housagotchi", "true", "false", mapping, PUBLISH_WITHIN_24_HOURS);
        HashMap<String, String> data = new HashMap<>();

        LocalDateTime publishDate = LocalDateTime.now();
        LocalDateTime now = LocalDateTime.of(2019, 5, 30, 16, 10, 12);
        LocalDateTime tomorrow = LocalDateTime.of(2019, 5, 31, 16, 10, 12);
        Clock clock = Clock.fixed(ZonedDateTime.of(2019, 5, 30, 16, 10, 12, 0, ZoneId.systemDefault()).toInstant(), ZoneId.systemDefault());

        Event event = new Event("Housagotchi", "flowId", FlowAction.START, publishDate, data);

        strategy = new PublishWithin24HoursPublishStrategy(notificationRepository, clock);

        // mock
        doReturn(new ArrayList<>()).when(notificationRepository).findNotificationsThatShouldBePublishedBetween(now, tomorrow);

        //execute
        LocalDateTime result = strategy.apply(new FiringSubscription(subscription, event));

        //assert
        verify(notificationRepository).findNotificationsThatShouldBePublishedBetween(now, tomorrow);
        verifyNoMoreInteractions(notificationRepository);

        assertEquals(now.plusDays(1).withHour(16).withMinute(0).withSecond(0), result);
    }
}
