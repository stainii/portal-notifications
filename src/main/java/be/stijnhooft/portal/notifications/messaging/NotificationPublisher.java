package be.stijnhooft.portal.notifications.messaging;

import be.stijnhooft.portal.notifications.entities.Notification;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
@EnableBinding(NotificationTopic.class)
@Slf4j
public class NotificationPublisher {

  private final NotificationTopic notificationTopic;

  @Autowired
  public NotificationPublisher(NotificationTopic notificationTopic) {
    this.notificationTopic = notificationTopic;
  }

  public void publish(Collection<Notification> notifications) {
      log.info("Sending notifications to the Notification topic");
      log.debug(notifications.toString());
      notificationTopic.notificationTopic().send(MessageBuilder.withPayload(notifications).build());
  }

}
