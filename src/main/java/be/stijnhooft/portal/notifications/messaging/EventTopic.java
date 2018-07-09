package be.stijnhooft.portal.notifications.messaging;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.MessageChannel;

public interface EventTopic {

    String INPUT = "eventTopic";

    @Input(INPUT)
    MessageChannel eventTopic();
}
