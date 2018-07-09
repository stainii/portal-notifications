package be.stijnhooft.portal.notifications.messaging;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

public interface NotificationTopic {

    String OUTPUT = "notificationTopic";

    @Output(OUTPUT)
    MessageChannel notificationTopic();
}
