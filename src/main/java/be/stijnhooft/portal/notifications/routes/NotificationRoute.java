package be.stijnhooft.portal.notifications.routes;

import be.stijnhooft.portal.notifications.mappers.NotificationMapper;
import be.stijnhooft.portal.notifications.model.Notification;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import org.apache.camel.ExchangePattern;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jackson.JacksonDataFormat;
import org.apache.camel.component.jackson.ListJacksonDataFormat;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

@Component
public class NotificationRoute extends RouteBuilder {

    @Inject
    private NotificationMapper mapper;

    @Override
    public void configure() throws Exception {
        JacksonDataFormat formatForListOfNotifications = new ListJacksonDataFormat(Notification.class);
        formatForListOfNotifications.addModule(new ParameterNamesModule());
        formatForListOfNotifications.addModule(new Jdk8Module());
        formatForListOfNotifications.addModule(new JavaTimeModule());
        formatForListOfNotifications.disableFeature(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        from("direct:publishService")
            .bean(mapper, "mapEntitiesToModel")
            .marshal(formatForListOfNotifications)
            .setExchangePattern(ExchangePattern.InOnly)
            .to("log:sending-out-notification?level=INFO")
            .to("activemq:topic:NotificationTopic?timeToLive=3600000");
    }

}
