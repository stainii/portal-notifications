package be.stijnhooft.portal.notifications.services;

import be.stijnhooft.portal.model.domain.Event;
import be.stijnhooft.portal.notifications.entities.NotificationEntity;
import be.stijnhooft.portal.notifications.mappers.NotificationMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class EventService {

  private final SubscriptionService subscriptionService;
  private final NotificationMapper notificationMapper;
  private final NotificationService notificationService;

  @Autowired
  public EventService(SubscriptionService subscriptionService, NotificationMapper notificationMapper, NotificationService notificationService) {
    this.subscriptionService = subscriptionService;
    this.notificationMapper = notificationMapper;
    this.notificationService = notificationService;
  }

  public void receiveEvents(Collection<Event> events) {
    log.info("Received events: " + events);

    List<NotificationEntity> notifications = events.parallelStream()
      .flatMap(subscriptionService::fireForEvent)
      .map(notificationMapper::map)
      .collect(Collectors.toList());

    if (notifications.isEmpty()) {
        log.info("Received events, but no subscriptions exist for these events. Doing nothing.");
    } else {
        notificationService.saveAndIfUrgentThenPublish(notifications);
    }
  }

}
