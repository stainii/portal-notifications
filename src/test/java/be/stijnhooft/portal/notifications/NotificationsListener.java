package be.stijnhooft.portal.notifications;

import be.stijnhooft.portal.model.notification.Notification;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class NotificationsListener {

    private final List<Notification> receivedNotifications = new ArrayList<>();
    private final ObjectMapper objectMapper;

    @RabbitListener(bindings = @QueueBinding(
        value = @org.springframework.amqp.rabbit.annotation.Queue(value = "notificationTestQueue", durable = "true"),
        exchange = @org.springframework.amqp.rabbit.annotation.Exchange(value = "notificationTopic", type = "topic"),
        key = "notificationTopic"
    ))
    public void receiveMessage(String message) throws JsonProcessingException {
        receivedNotifications.addAll(objectMapper.readValue(message, new TypeReference<List<Notification>>() {
        }));
    }

    public List<Notification> getReceivedNotifications() {
        return new ArrayList<>(receivedNotifications);
    }

    public void reset() {
        receivedNotifications.clear();
    }

}
