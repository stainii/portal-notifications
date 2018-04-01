package be.stijnhooft.portal.notifications.routes;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.springframework.stereotype.Component;

@Component
public class EventRoute extends RouteBuilder {

    @Override
    public void configure() {
        from("activemq:topic:EventTopic?timeToLive=3600000")
          .unmarshal()
          .json(JsonLibrary.Jackson)
          .to("bean:eventService?method=receiveEvents(${body})");
    }

}
