package be.stijnhooft.portal.notifications.schedulers;

import be.stijnhooft.portal.notifications.entities.NotificationEntity;
import be.stijnhooft.portal.notifications.mappers.NotificationMapper;
import be.stijnhooft.portal.notifications.messaging.NotificationPublisher;
import be.stijnhooft.portal.notifications.model.Notification;
import be.stijnhooft.portal.notifications.repositories.NotificationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Component
@Slf4j
@EnableScheduling
@Transactional
public class PublishNotifications {

    private static final String EVERY_MINUTE = "0   *  *   *   *  *";
    private final NotificationRepository notificationRepository;
    private final NotificationPublisher notificationPublisher;
    private final NotificationMapper notificationMapper;
    private final Clock clock;

    @Autowired
    public PublishNotifications(NotificationRepository notificationRepository,
                                NotificationPublisher notificationPublisher,
                                NotificationMapper notificationMapper,
                                Clock clock) {
        this.notificationRepository = notificationRepository;
        this.notificationPublisher = notificationPublisher;
        this.notificationMapper = notificationMapper;
        this.clock = clock;
    }

    @Scheduled(cron = EVERY_MINUTE) // note: Spring makes sure that, if the method has not finished its work within the minute, the next invocation of this method will NOT occur until the previous, time-consuming method has finished. Don't be scared of overlap.
    public void publishNotifications() {
        LocalDateTime lastPublishDate = findLastPublishDate();

        List<NotificationEntity> notificationEntities =
            notificationRepository.findNotificationsThatShouldBePublishedBetween(lastPublishDate, LocalDateTime.ofInstant(clock.instant(), ZoneId.systemDefault()));

        if (notificationEntities.isEmpty()) {
            log.info("No notifications to publish right now.");
        } else {
            publishNotifications(notificationEntities);
        }

    }

    private void publishNotifications(List<NotificationEntity> notificationEntities) {
        log.info("Publishing " + notificationEntities.size() + " notifications.");
        log.debug(notificationEntities.toString());

        List<Notification> notifications = notificationMapper.mapEntitiesToModel(notificationEntities);
        notificationPublisher.publish(notifications);

        notificationEntities.forEach(notificationEntity -> notificationEntity.setPublished(true));
    }

    private LocalDateTime findLastPublishDate() {
        LocalDateTime lastPublishDate = notificationRepository.findLastPublishDate();
        if (lastPublishDate == null) {
            log.info("This could be the first time that notifications are published");
            return LocalDateTime.MIN;
        } else {
            log.info("Last time that notifications were published was " + lastPublishDate + ".");
            return lastPublishDate;
        }
    }

}
