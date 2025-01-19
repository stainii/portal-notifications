package be.stijnhooft.portal.notifications.schedulers;

import be.stijnhooft.portal.model.notification.Notification;
import be.stijnhooft.portal.notifications.entities.NotificationEntity;
import be.stijnhooft.portal.notifications.mappers.NotificationMapper;
import be.stijnhooft.portal.notifications.messaging.NotificationPublisher;
import be.stijnhooft.portal.notifications.repositories.NotificationRepository;
import lombok.extern.slf4j.Slf4j;
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

    private final NotificationRepository notificationRepository;
    private final NotificationPublisher notificationPublisher;
    private final NotificationMapper notificationMapper;
    private final Clock clock;

    public PublishNotifications(NotificationRepository notificationRepository,
                                NotificationPublisher notificationPublisher,
                                NotificationMapper notificationMapper,
                                Clock clock) {
        this.notificationRepository = notificationRepository;
        this.notificationPublisher = notificationPublisher;
        this.notificationMapper = notificationMapper;
        this.clock = clock;
    }

    @Scheduled(cron = "${portal-notifications.schedule.cron}")
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
            return LocalDateTime.of(2018, 1, 1, 0, 0);
        } else {
            log.info("Last time that notifications were published was " + lastPublishDate + ".");
            return lastPublishDate;
        }
    }

}
