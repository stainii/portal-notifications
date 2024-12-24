package be.stijnhooft.portal.notifications.mappers;

import be.stijnhooft.portal.model.domain.Event;
import be.stijnhooft.portal.model.domain.FlowAction;
import be.stijnhooft.portal.model.notification.Notification;
import be.stijnhooft.portal.model.notification.PublishStrategy;
import be.stijnhooft.portal.notifications.dtos.FiringSubscription;
import be.stijnhooft.portal.notifications.entities.NotificationActionEmbeddable;
import be.stijnhooft.portal.notifications.entities.NotificationEntity;
import be.stijnhooft.portal.notifications.entities.SubscriptionEntity;
import be.stijnhooft.portal.notifications.entities.SubscriptionMappingToNotificationEmbeddable;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.HashMap;

import static be.stijnhooft.portal.model.notification.PublishStrategy.PUBLISH_AT_SPECIFIC_DATE_TIME;
import static be.stijnhooft.portal.model.notification.PublishStrategy.PUBLISH_IMMEDIATELY;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("local")
public class NotificationMapperTest {

    @Autowired
    private NotificationMapper notificationMapper;

    @Test
    public void mapWhenTheNotificationNeedsToBePublishedAtASpecificDateTime() {
        //data set
        String mappingOfTitle = "data['taskName']";
        String mappingOfMessage = "'Task ' + data['taskName'] + ' is expiring. Due date was ' + data['dueDate'] + '.'";
        String mappingOfActionText = "'Open Housagotchi'";
        String mappingOfActionUrl = "data['url']";
        String flowId = "flowId";
        String mappingOfScheduleDate = "'2019-10-11T09:30:00'";
        SubscriptionMappingToNotificationEmbeddable mapping = new SubscriptionMappingToNotificationEmbeddable(mappingOfTitle, mappingOfMessage, mappingOfActionText, mappingOfActionUrl, mappingOfScheduleDate);

        SubscriptionEntity subscription = new SubscriptionEntity(1L, "Housagotchi", "true", "false", mapping, PUBLISH_AT_SPECIFIC_DATE_TIME);

        HashMap<String, String> data = new HashMap<>();
        data.put("taskName", "Ironing");
        data.put("dueDate", "08/04/2018");
        data.put("url", "http://housagotchi");

        LocalDateTime publishDate = LocalDateTime.now();
        Event event = new Event("Housagotchi", flowId, FlowAction.START, publishDate, data);

        //execute
        NotificationEntity result = notificationMapper.map(new FiringSubscription(subscription, event));

        //assert
        assertEquals("Ironing", result.getTitle());
        assertEquals("Task Ironing is expiring. Due date was 08/04/2018.", result.getMessage());
        assertEquals("Housagotchi", result.getOrigin());
        assertEquals("Open Housagotchi", result.getAction().getText());
        assertEquals("http://housagotchi", result.getAction().getInternalUrl());
        assertEquals(PUBLISH_AT_SPECIFIC_DATE_TIME, result.getPublishStrategy());
        assertEquals(flowId, result.getFlowId());
        assertEquals(LocalDateTime.of(2019, 10, 11, 9, 30), result.getScheduledAt());
        assertNull(result.getCancelledAt());
        assertNull(result.getId());
        assertFalse(result.isRead());
        assertFalse(result.isPublished());
    }

    @Test
    public void mapWhenTheNotificationNeedsToBePublishedImmediately() {
        //data set
        String mappingOfTitle = "data['taskName']";
        String mappingOfMessage = "'Task ' + data['taskName'] + ' is expiring. Due date was ' + data['dueDate'] + '.'";
        String mappingOfActionText = "'Open Housagotchi'";
        String mappingOfActionUrl = "data['url']";
        String flowId = "flowId";
        String mappingOfScheduleDate = "'2019-10-11T09:30:00.0000+02:00'";
        SubscriptionMappingToNotificationEmbeddable mapping = new SubscriptionMappingToNotificationEmbeddable(mappingOfTitle, mappingOfMessage, mappingOfActionText, mappingOfActionUrl, mappingOfScheduleDate);

        SubscriptionEntity subscription = new SubscriptionEntity(1L, "Housagotchi", "true", "false", mapping, PUBLISH_IMMEDIATELY);

        HashMap<String, String> data = new HashMap<>();
        data.put("taskName", "Ironing");
        data.put("dueDate", "08/04/2018");
        data.put("url", "http://housagotchi");

        LocalDateTime publishDate = LocalDateTime.now();
        Event event = new Event("Housagotchi", flowId, FlowAction.START, publishDate, data);

        //execute
        NotificationEntity result = notificationMapper.map(new FiringSubscription(subscription, event));

        //assert
        assertEquals("Ironing", result.getTitle());
        assertEquals("Task Ironing is expiring. Due date was 08/04/2018.", result.getMessage());
        assertEquals("Housagotchi", result.getOrigin());
        assertEquals("Open Housagotchi", result.getAction().getText());
        assertEquals("http://housagotchi", result.getAction().getInternalUrl());
        assertEquals(PUBLISH_IMMEDIATELY, result.getPublishStrategy());
        assertEquals(flowId, result.getFlowId());
        assertNotEquals(LocalDateTime.of(2019, 10, 11, 9, 30), result.getScheduledAt());
        assertNull(result.getCancelledAt());
        assertNull(result.getId());
        assertFalse(result.isRead());
        assertFalse(result.isPublished());
    }

    @Test
    public void mapWhenProvidingNull() {
        assertThrows(NullPointerException.class, () ->
            notificationMapper.map(null));
    }

    @Test
    public void mapEntityToModel() {
        Long id = 1L;
        String origin = "housagotchi";
        LocalDateTime creationDateAndPublishDate = LocalDateTime.now();
        String title = "title";
        String message = "message";
        String actionUrl = "url";
        String actionText = "text";
        String flowId = "f-id";
        LocalDateTime cancellationDate = LocalDateTime.now();
        NotificationActionEmbeddable action = new NotificationActionEmbeddable(actionUrl, actionText);
        PublishStrategy publishStrategy = PUBLISH_IMMEDIATELY;
        NotificationEntity entity = new NotificationEntity(id, origin, flowId, title, message, action, publishStrategy, creationDateAndPublishDate, creationDateAndPublishDate, cancellationDate, false, false);

        Notification model = notificationMapper.mapEntityToModel(entity);

        assertEquals(id, model.getId());
        assertEquals(origin, model.getOrigin());
        assertEquals(creationDateAndPublishDate, model.getDate());
        assertEquals(title, model.getTitle());
        assertEquals(message, model.getMessage());
        assertEquals("https://please_overwrite/api/notification/1/action/url/", model.getAction().getUrl());
        assertEquals("url", model.getAction().getInternalUrl());
        assertEquals(actionText, model.getAction().getText());
        assertEquals(publishStrategy, model.getPublishStrategy());
    }


    @Test
    public void mapEntityToModelWhenProvidingNull() {
        assertThrows(NullPointerException.class, () ->
            notificationMapper.mapEntityToModel(null));
    }
}
