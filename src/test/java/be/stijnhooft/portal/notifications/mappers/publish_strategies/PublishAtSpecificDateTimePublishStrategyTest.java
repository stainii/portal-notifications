package be.stijnhooft.portal.notifications.mappers.publish_strategies;

import be.stijnhooft.portal.model.domain.Event;
import be.stijnhooft.portal.notifications.dtos.FiringSubscription;
import be.stijnhooft.portal.notifications.entities.SubscriptionEntity;
import be.stijnhooft.portal.notifications.entities.SubscriptionMappingToNotificationEmbeddable;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDateTime;
import java.util.HashMap;

import static be.stijnhooft.portal.notifications.model.PublishStrategy.PUBLISH_AT_SPECIFIC_DATE_TIME;
import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class PublishAtSpecificDateTimePublishStrategyTest {

    @InjectMocks
    private PublishAtSpecificDateTimePublishStrategy strategy;

    @Test
    public void applyWhenSuccess() {
        //data set
        String mappingOfScheduleDate = "data['dueDate']";
        SubscriptionMappingToNotificationEmbeddable mapping = new SubscriptionMappingToNotificationEmbeddable("", "", "", "", mappingOfScheduleDate);

        SubscriptionEntity subscription = new SubscriptionEntity(1L, "Housagotchi", "true", "false", mapping, PUBLISH_AT_SPECIFIC_DATE_TIME);

        HashMap<String, String> data = new HashMap<>();
        data.put("dueDate", "2019-10-11T09:30:00");

        LocalDateTime publishDate = LocalDateTime.now();
        Event event = new Event("Housagotchi", "flowId", publishDate, data);

        //execute
        LocalDateTime result = strategy.apply(new FiringSubscription(subscription, event));

        //assert
        assertEquals(LocalDateTime.of(2019, 10, 11, 9, 30), result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void applyWhenMappingOfScheduleDateIsNotProvided() {
        //data set
        SubscriptionMappingToNotificationEmbeddable mapping = new SubscriptionMappingToNotificationEmbeddable("", "", "", "",  null);

        SubscriptionEntity subscription = new SubscriptionEntity(1L, "Housagotchi", "true", "false", mapping, PUBLISH_AT_SPECIFIC_DATE_TIME);

        HashMap<String, String> data = new HashMap<>();
        data.put("dueDate", "2019-10-11T09:30:00");

        LocalDateTime publishDate = LocalDateTime.now();
        Event event = new Event("Housagotchi", "flowId", publishDate, data);

        //execute
        strategy.apply(new FiringSubscription(subscription, event));
    }

    @Test(expected = IllegalArgumentException.class)
    public void applyWhenMappingOfScheduleDateIsEmpty() {
        //data set
        SubscriptionMappingToNotificationEmbeddable mapping = new SubscriptionMappingToNotificationEmbeddable("", "", "", "",  "");

        SubscriptionEntity subscription = new SubscriptionEntity(1L, "Housagotchi", "true", "false", mapping, PUBLISH_AT_SPECIFIC_DATE_TIME);

        HashMap<String, String> data = new HashMap<>();
        data.put("dueDate", "2019-10-11T09:30:00");

        LocalDateTime publishDate = LocalDateTime.now();
        Event event = new Event("Housagotchi", "flowId", publishDate, data);

        //execute
        strategy.apply(new FiringSubscription(subscription, event));
    }

}
