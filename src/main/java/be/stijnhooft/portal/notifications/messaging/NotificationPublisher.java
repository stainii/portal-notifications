package be.stijnhooft.portal.notifications.messaging;

import be.stijnhooft.portal.model.notification.Notification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
@Slf4j
@RequiredArgsConstructor
public class NotificationPublisher {

    private final StreamBridge streamBridge;

    public void publish(Collection<Notification> notifications) {
        log.info("Sending notifications to the Notification channel");
        log.debug(notifications.toString());
        streamBridge.send("notificationChannel-out-0", notifications);
    }

}
