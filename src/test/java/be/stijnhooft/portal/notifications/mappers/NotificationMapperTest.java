package be.stijnhooft.portal.notifications.mappers;

import be.stijnhooft.portal.model.domain.Event;
import be.stijnhooft.portal.notifications.dtos.FiringSubscription;
import be.stijnhooft.portal.notifications.entities.NotificationActionEmbeddable;
import be.stijnhooft.portal.notifications.entities.NotificationEntity;
import be.stijnhooft.portal.notifications.entities.SubscriptionEntity;
import be.stijnhooft.portal.notifications.entities.SubscriptionMappingToNotificationEmbeddable;
import be.stijnhooft.portal.notifications.model.Notification;
import be.stijnhooft.portal.notifications.model.NotificationUrgency;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.HashMap;

import static be.stijnhooft.portal.notifications.model.NotificationUrgency.PUBLISH_IMMEDIATELY;
import static be.stijnhooft.portal.notifications.model.NotificationUrgency.PUBLISH_WITHIN_24_HOURS;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("local")
public class NotificationMapperTest {

    @Autowired
    private NotificationMapper notificationMapper;

    @Test
    public void map() {
        //data set
        String mappingOfTitle = "data['taskName']";
        String mappingOfMessage = "'Task ' + data['taskName'] + ' is expiring. Due date was ' + data['dueDate'] + '.'";
        String mappingOfActionText = "'Open Housagotchi'";
        String mappingOfActionUrl = "data['url']";
        String flowId = "flowId";
        SubscriptionMappingToNotificationEmbeddable mapping = new SubscriptionMappingToNotificationEmbeddable(mappingOfTitle, mappingOfMessage, mappingOfActionText, mappingOfActionUrl);

        SubscriptionEntity subscription = new SubscriptionEntity(1L, "Housagotchi", "true", "false", mapping, PUBLISH_IMMEDIATELY);

        HashMap<String, String> data = new HashMap<>();
        data.put("taskName", "Ironing");
        data.put("dueDate", "08/04/2018");
        data.put("url", "http://housagotchi");

        LocalDateTime publishDate = LocalDateTime.now();
        Event event = new Event("Housagotchi", flowId, publishDate, data);

        //execute
        NotificationEntity result = notificationMapper.map(new FiringSubscription(subscription, event));

        //assert
        assertEquals("Ironing", result.getTitle());
        assertEquals("Task Ironing is expiring. Due date was 08/04/2018.", result.getMessage());
        assertEquals("Housagotchi", result.getOrigin());
        assertEquals(publishDate, result.getDate());
        assertEquals("Open Housagotchi", result.getAction().getText());
        assertEquals("http://housagotchi", result.getAction().getInternalUrl());
        assertEquals(PUBLISH_IMMEDIATELY, result.getUrgency());
        assertEquals(flowId, result.getFlowId());
        assertNull(result.getCancelledAt());
        assertNull(result.getId());
    }

    @Test(expected = NullPointerException.class)
    public void mapWhenProvidingNull() {
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
        String flowId = "f-id";
        LocalDateTime cancellationDate = LocalDateTime.now();
        NotificationActionEmbeddable action = new NotificationActionEmbeddable(actionUrl, actionText);
        NotificationUrgency urgency = PUBLISH_WITHIN_24_HOURS;
        NotificationEntity entity = new NotificationEntity(id, origin, flowId, date, title, message, action, urgency, false, cancellationDate);

        Notification model = notificationMapper.mapEntityToModel(entity);

        assertEquals(id, model.getId());
        assertEquals(origin, model.getOrigin());
        assertEquals(date, model.getDate());
        assertEquals(title, model.getTitle());
        assertEquals(message, model.getMessage());
        assertEquals("https://please_overwrite/api/notification/1/action/url/", model.getAction().getUrl());
        assertEquals("url", model.getAction().getInternalUrl());
        assertEquals(actionText, model.getAction().getText());
        assertEquals(urgency, model.getUrgency());
    }

    @Test(expected = NullPointerException.class)
    public void mapEntityToModelWhenProvidingNull() {
        notificationMapper.mapEntityToModel(null);
    }
}
