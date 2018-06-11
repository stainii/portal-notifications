package be.stijnhooft.portal.notifications.mappers;

import be.stijnhooft.portal.model.domain.Event;
import be.stijnhooft.portal.notifications.dtos.FiringSubscription;
import be.stijnhooft.portal.notifications.entities.Notification;
import be.stijnhooft.portal.notifications.entities.NotificationAction;
import be.stijnhooft.portal.notifications.entities.Subscription;
import be.stijnhooft.portal.notifications.entities.SubscriptionMappingToNotification;
import be.stijnhooft.portal.notifications.model.NotificationUrgency;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.HashMap;

import static be.stijnhooft.portal.notifications.model.NotificationUrgency.PUBLISH_IMMEDIATELY;
import static be.stijnhooft.portal.notifications.model.NotificationUrgency.PUBLISH_WITHIN_24_HOURS;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@RunWith(SpringRunner.class)
public class NotificationMapperTest {

  @InjectMocks
  private NotificationMapper notificationMapper;

  @Test
  public void map() {
    //data set
    String mappingOfTitle = "data['taskName']";
    String mappingOfMessage = "'Task ' + data['taskName'] + ' is expiring. Due date was ' + data['dueDate'] + '.'";
    String mappingOfActionText = "'Open Housagotchi'";
    String mappingOfActionUrl = "data['url']";
    SubscriptionMappingToNotification mapping = new SubscriptionMappingToNotification(mappingOfTitle, mappingOfMessage, mappingOfActionText, mappingOfActionUrl);

    Subscription subscription = new Subscription(1L, "Housagotchi", "true", mapping, PUBLISH_IMMEDIATELY);

    HashMap<String, String> data = new HashMap<>();
    data.put("taskName", "Ironing");
    data.put("dueDate", "08/04/2018");
    data.put("url", "http://housagotchi");

    LocalDateTime publishDate = LocalDateTime.now();
    Event event = new Event("Housagotchi", publishDate, data);

    //execute
    Notification result = notificationMapper.map(new FiringSubscription(subscription, event));

    //assert
    assertEquals("Ironing", result.getTitle());
    assertEquals("Task Ironing is expiring. Due date was 08/04/2018.", result.getMessage());
    assertEquals("Housagotchi", result.getOrigin());
    assertEquals(publishDate, result.getDate());
    assertEquals("Open Housagotchi", result.getAction().getText());
    assertEquals("http://housagotchi", result.getAction().getUrl());
    assertEquals(PUBLISH_IMMEDIATELY, result.getUrgency());
    assertNull(result.getId());
  }

  @Test(expected = NullPointerException.class)
  public void mapWHenProvidingNull() {
    notificationMapper.map(null);
  }

  @Test
  public void mapEntityToModel() {
    Long id = 1L;
    String origin = "housagotchi";
    LocalDateTime date = LocalDateTime.now();
    String title = "title";
    String message = "message";
    String actionUrl = "url";
    String actionText = "text";
    NotificationAction action = new NotificationAction(actionUrl, actionText);
    NotificationUrgency urgency = PUBLISH_WITHIN_24_HOURS;
    Notification entity = new Notification(id, origin, date, title, message, action, urgency, false);

    be.stijnhooft.portal.notifications.model.Notification model = notificationMapper.mapEntityToModel(entity);

    assertEquals(id, model.getId());
    assertEquals(origin, model.getOrigin());
    assertEquals(date, model.getDate());
    assertEquals(title, model.getTitle());
    assertEquals(message, model.getMessage());
    assertEquals(actionUrl, model.getAction().getUrl());
    assertEquals(actionText, model.getAction().getText());
    assertEquals(urgency, model.getUrgency());
  }

  @Test(expected = NullPointerException.class)
  public void mapEntityToModelWhenProvidingNull() {
    notificationMapper.mapEntityToModel(null);
  }
}
