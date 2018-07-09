package be.stijnhooft.portal.notifications.messaging;

import be.stijnhooft.portal.model.domain.Event;
import be.stijnhooft.portal.notifications.services.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@EnableBinding(EventTopic.class)
public class EventTopicListener {

    private final EventService eventService;

    @Autowired
    public EventTopicListener(EventService eventService) {
        this.eventService = eventService;
    }

    @StreamListener(EventTopic.INPUT)
    public void log(List<Event> events) {
        System.out.println(events);
        eventService.receiveEvents(events);
    }

}
