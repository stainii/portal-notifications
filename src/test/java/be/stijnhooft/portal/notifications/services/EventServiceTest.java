package be.stijnhooft.portal.notifications.services;

import be.stijnhooft.portal.model.domain.Event;
import be.stijnhooft.portal.notifications.dtos.FiringSubscription;
import be.stijnhooft.portal.notifications.entities.NotificationActionEmbeddable;
import be.stijnhooft.portal.notifications.entities.NotificationEntity;
import be.stijnhooft.portal.notifications.entities.SubscriptionEntity;
import be.stijnhooft.portal.notifications.mappers.NotificationMapper;
import be.stijnhooft.portal.notifications.model.PublishStrategy;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.stream.Stream;

import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
public class EventServiceTest {

    @InjectMocks
    private EventService eventService;

    @Mock
    private NotificationService notificationService;

    @Mock
    private NotificationMapper notificationMapper;

    @Mock
    private SubscriptionService subscriptionService;

    @Test
    public void receiveEventsWithOnlyActivationEvents() {
        //data set
        Event event1 = new Event("source1", "flow1", LocalDateTime.now(), new HashMap<>());
        Event event2 = new Event("source2", "flow2", LocalDateTime.now(), new HashMap<>());
        Event event3 = new Event("source3", "flow3", LocalDateTime.now(), new HashMap<>());

        FiringSubscription firingSubscription1 = new FiringSubscription(new SubscriptionEntity(), event1);
        FiringSubscription firingSubscription2 = new FiringSubscription(new SubscriptionEntity(), event3);

        NotificationEntity notification1 = new NotificationEntity("source1", "flow1", "1", "1", new NotificationActionEmbeddable("1", "1"), PublishStrategy.PUBLISH_AT_SPECIFIC_DATE_TIME, LocalDateTime.now(), LocalDateTime.now());
        NotificationEntity notification2 = new NotificationEntity("source3", "flow3", "3", "3", new NotificationActionEmbeddable("3", "3"), PublishStrategy.PUBLISH_IMMEDIATELY, LocalDateTime.now(), LocalDateTime.now());

        //mock
        doReturn(Stream.of(firingSubscription1)).when(subscriptionService).fireOnActivationCondition(event1);
        doReturn(Stream.empty()).when(subscriptionService).fireOnActivationCondition(event2);
        doReturn(Stream.of(firingSubscription2)).when(subscriptionService).fireOnActivationCondition(event3);

        doReturn(notification1).when(notificationMapper).map(firingSubscription1);
        doReturn(notification2).when(notificationMapper).map(firingSubscription2);

        doReturn(Stream.empty()).when(subscriptionService).fireOnCancellationCondition(event1);
        doReturn(Stream.empty()).when(subscriptionService).fireOnCancellationCondition(event2);
        doReturn(Stream.empty()).when(subscriptionService).fireOnCancellationCondition(event3);

        //execute
        eventService.receiveEvents(Arrays.asList(event1, event2, event3));

        //verify
        verify(subscriptionService).fireOnActivationCondition(event1);
        verify(subscriptionService).fireOnActivationCondition(event2);
        verify(subscriptionService).fireOnActivationCondition(event3);

        verify(notificationMapper).map(firingSubscription1);
        verify(notificationMapper).map(firingSubscription2);

        verify(notificationService).saveAndIfUrgentThenPublish(Arrays.asList(notification1, notification2));

        verify(subscriptionService).fireOnCancellationCondition(event1);
        verify(subscriptionService).fireOnCancellationCondition(event2);
        verify(subscriptionService).fireOnCancellationCondition(event3);

        verifyNoMoreInteractions(subscriptionService, notificationMapper, notificationService);
    }

    @Test
    public void receiveEventsWithOnlyCancellationEvents() {
        //data set
        Event event1 = new Event("source1", "flow1", LocalDateTime.now(), new HashMap<>());
        Event event2 = new Event("source2", "flow2", LocalDateTime.now(), new HashMap<>());
        Event event3 = new Event("source3", "flow3", LocalDateTime.now(), new HashMap<>());

        FiringSubscription firingSubscription1 = new FiringSubscription(new SubscriptionEntity(), event1);
        FiringSubscription firingSubscription2 = new FiringSubscription(new SubscriptionEntity(), event3);

        NotificationEntity notification1 = new NotificationEntity("source1", "flow1", "1", "1", new NotificationActionEmbeddable("1", "1"), PublishStrategy.PUBLISH_AT_SPECIFIC_DATE_TIME, LocalDateTime.now(), LocalDateTime.now());
        NotificationEntity notification2 = new NotificationEntity("source3", "flow3", "3", "3", new NotificationActionEmbeddable("3", "3"), PublishStrategy.PUBLISH_IMMEDIATELY, LocalDateTime.now(), LocalDateTime.now());

        //mock
        doReturn(Stream.empty()).when(subscriptionService).fireOnActivationCondition(event1);
        doReturn(Stream.empty()).when(subscriptionService).fireOnActivationCondition(event2);
        doReturn(Stream.empty()).when(subscriptionService).fireOnActivationCondition(event3);

        doReturn(notification1).when(notificationMapper).map(firingSubscription1);
        doReturn(notification2).when(notificationMapper).map(firingSubscription2);

        doReturn(Stream.of(firingSubscription1)).when(subscriptionService).fireOnCancellationCondition(event1);
        doReturn(Stream.empty()).when(subscriptionService).fireOnCancellationCondition(event2);
        doReturn(Stream.of(firingSubscription2)).when(subscriptionService).fireOnCancellationCondition(event3);

        //execute
        eventService.receiveEvents(Arrays.asList(event1, event2, event3));

        //verify
        verify(subscriptionService).fireOnActivationCondition(event1);
        verify(subscriptionService).fireOnActivationCondition(event2);
        verify(subscriptionService).fireOnActivationCondition(event3);

        verify(subscriptionService).fireOnCancellationCondition(event1);
        verify(subscriptionService).fireOnCancellationCondition(event2);
        verify(subscriptionService).fireOnCancellationCondition(event3);

        verify(notificationService).cancelNotifications(Arrays.asList(event1, event3));

        verifyNoMoreInteractions(subscriptionService, notificationMapper, notificationService);
    }

    @Test
    public void receiveEventsWithAllEvents() {
        //data set
        Event event1 = new Event("source1", "flow1", LocalDateTime.now(), new HashMap<>());
        Event event2 = new Event("source2", "flow2", LocalDateTime.now(), new HashMap<>());
        Event event3 = new Event("source3", "flow3", LocalDateTime.now(), new HashMap<>());

        FiringSubscription firingSubscription1 = new FiringSubscription(new SubscriptionEntity(), event1);
        FiringSubscription firingSubscription2 = new FiringSubscription(new SubscriptionEntity(), event3);

        NotificationEntity notification1 = new NotificationEntity("source1", "flow1", "1", "1", new NotificationActionEmbeddable("1", "1"), PublishStrategy.PUBLISH_AT_SPECIFIC_DATE_TIME, LocalDateTime.now(), LocalDateTime.now());

        //mock
        doReturn(Stream.of(firingSubscription1)).when(subscriptionService).fireOnActivationCondition(event1);
        doReturn(Stream.empty()).when(subscriptionService).fireOnActivationCondition(event2);
        doReturn(Stream.empty()).when(subscriptionService).fireOnActivationCondition(event3);

        doReturn(notification1).when(notificationMapper).map(firingSubscription1);

        doReturn(Stream.empty()).when(subscriptionService).fireOnCancellationCondition(event1);
        doReturn(Stream.empty()).when(subscriptionService).fireOnCancellationCondition(event2);
        doReturn(Stream.of(firingSubscription2)).when(subscriptionService).fireOnCancellationCondition(event3);

        //execute
        eventService.receiveEvents(Arrays.asList(event1, event2, event3));

        //verify
        verify(subscriptionService).fireOnActivationCondition(event1);
        verify(subscriptionService).fireOnActivationCondition(event2);
        verify(subscriptionService).fireOnActivationCondition(event3);

        verify(notificationMapper).map(firingSubscription1);

        verify(notificationService).saveAndIfUrgentThenPublish(Arrays.asList(notification1));

        verify(subscriptionService).fireOnCancellationCondition(event1);
        verify(subscriptionService).fireOnCancellationCondition(event2);
        verify(subscriptionService).fireOnCancellationCondition(event3);

        verify(notificationService).cancelNotifications(Arrays.asList(event3));

        verifyNoMoreInteractions(subscriptionService, notificationMapper, notificationService);
    }

    @Test
    public void receiveEventsButNothingShouldFire() {
        //data set
        Event event1 = new Event("source1", "flow1", LocalDateTime.now(), new HashMap<>());
        Event event2 = new Event("source2", "flow2", LocalDateTime.now(), new HashMap<>());
        Event event3 = new Event("source3", "flow3", LocalDateTime.now(), new HashMap<>());

        //mock
        doReturn(Stream.empty()).when(subscriptionService).fireOnActivationCondition(event1);
        doReturn(Stream.empty()).when(subscriptionService).fireOnActivationCondition(event2);
        doReturn(Stream.empty()).when(subscriptionService).fireOnActivationCondition(event3);

        doReturn(Stream.empty()).when(subscriptionService).fireOnCancellationCondition(event1);
        doReturn(Stream.empty()).when(subscriptionService).fireOnCancellationCondition(event2);
        doReturn(Stream.empty()).when(subscriptionService).fireOnCancellationCondition(event3);

        //execute
        eventService.receiveEvents(Arrays.asList(event1, event2, event3));

        //verify
        verify(subscriptionService).fireOnActivationCondition(event1);
        verify(subscriptionService).fireOnActivationCondition(event2);
        verify(subscriptionService).fireOnActivationCondition(event3);

        verify(subscriptionService).fireOnCancellationCondition(event1);
        verify(subscriptionService).fireOnCancellationCondition(event2);
        verify(subscriptionService).fireOnCancellationCondition(event3);

        verifyNoMoreInteractions(subscriptionService, notificationMapper, notificationService);
    }
}
