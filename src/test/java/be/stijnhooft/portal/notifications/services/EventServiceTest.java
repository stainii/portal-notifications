package be.stijnhooft.portal.notifications.services;

import be.stijnhooft.portal.model.domain.Event;
import be.stijnhooft.portal.notifications.dtos.FiringSubscription;
import be.stijnhooft.portal.notifications.entities.NotificationActionEmbeddable;
import be.stijnhooft.portal.notifications.entities.NotificationEntity;
import be.stijnhooft.portal.notifications.entities.SubscriptionEntity;
import be.stijnhooft.portal.notifications.mappers.NotificationMapper;
import be.stijnhooft.portal.notifications.model.NotificationUrgency;
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
  public void receiveEvents() {
    //data set
    Event event1 = new Event("source1", LocalDateTime.now(), new HashMap<>());
    Event event2 = new Event("source2", LocalDateTime.now(), new HashMap<>());
    Event event3 = new Event("source3", LocalDateTime.now(), new HashMap<>());

    FiringSubscription firingSubscription1 = new FiringSubscription(new SubscriptionEntity(), event1);
    FiringSubscription firingSubscription2 = new FiringSubscription(new SubscriptionEntity(), event3);

    NotificationEntity notification1 = new NotificationEntity(null, "source1", LocalDateTime.now(), "1", "1", new NotificationActionEmbeddable("1", "1"), NotificationUrgency.PUBLISH_WITHIN_24_HOURS, false);
    NotificationEntity notification2 = new NotificationEntity(null, "source3", LocalDateTime.now(), "3", "3", new NotificationActionEmbeddable("3", "3"), NotificationUrgency.PUBLISH_IMMEDIATELY, false);

    //mock
    doReturn(Stream.of(firingSubscription1)).when(subscriptionService).fireForEvent(event1);
    doReturn(Stream.empty()).when(subscriptionService).fireForEvent(event2);
    doReturn(Stream.of(firingSubscription2)).when(subscriptionService).fireForEvent(event3);

    doReturn(notification1).when(notificationMapper).map(firingSubscription1);
    doReturn(notification2).when(notificationMapper).map(firingSubscription2);

    //execute
    eventService.receiveEvents(Arrays.asList(event1, event2, event3));

    //verify
    verify(subscriptionService).fireForEvent(event1);
    verify(subscriptionService).fireForEvent(event2);
    verify(subscriptionService).fireForEvent(event3);

    verify(notificationMapper).map(firingSubscription1);
    verify(notificationMapper).map(firingSubscription2);

    verify(notificationService).saveAndIfUrgentThenPublish(Arrays.asList(notification1, notification2));

    verifyNoMoreInteractions(subscriptionService, notificationMapper, notificationService);
  }

    @Test
    public void receiveEventsButNothingShouldFire() {
        //data set
        Event event1 = new Event("source1", LocalDateTime.now(), new HashMap<>());
        Event event2 = new Event("source2", LocalDateTime.now(), new HashMap<>());
        Event event3 = new Event("source3", LocalDateTime.now(), new HashMap<>());

        //mock
        doReturn(Stream.empty()).when(subscriptionService).fireForEvent(event1);
        doReturn(Stream.empty()).when(subscriptionService).fireForEvent(event2);
        doReturn(Stream.empty()).when(subscriptionService).fireForEvent(event3);

        //execute
        eventService.receiveEvents(Arrays.asList(event1, event2, event3));

        //verify
        verify(subscriptionService).fireForEvent(event1);
        verify(subscriptionService).fireForEvent(event2);
        verify(subscriptionService).fireForEvent(event3);

        verifyNoMoreInteractions(subscriptionService, notificationMapper, notificationService);
    }
}