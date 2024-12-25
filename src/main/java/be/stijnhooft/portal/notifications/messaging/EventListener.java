package be.stijnhooft.portal.notifications.messaging;

import be.stijnhooft.portal.model.domain.Event;
import be.stijnhooft.portal.notifications.services.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.function.Consumer;

@Configuration
@RequiredArgsConstructor
public class EventListener {

    private final EventService eventService;

    @Bean
    public Consumer<List<Event>> eventChannel() {
        return eventService::receiveEvents;
    }

}
