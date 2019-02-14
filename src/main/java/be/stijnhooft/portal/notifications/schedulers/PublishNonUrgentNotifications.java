package be.stijnhooft.portal.notifications.schedulers;

import be.stijnhooft.portal.notifications.entities.NotificationEntity;
import be.stijnhooft.portal.notifications.mappers.NotificationMapper;
import be.stijnhooft.portal.notifications.messaging.NotificationPublisher;
import be.stijnhooft.portal.notifications.model.Notification;
import be.stijnhooft.portal.notifications.model.NotificationUrgency;
import be.stijnhooft.portal.notifications.repositories.NotificationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@Slf4j
public class PublishNonUrgentNotifications {

    private final NotificationRepository notificationRepository;
    private final NotificationPublisher notificationPublisher;
    private final NotificationMapper notificationMapper;

    @Autowired
    public PublishNonUrgentNotifications(NotificationRepository notificationRepository,
                                         NotificationPublisher notificationPublisher,
                                         NotificationMapper notificationMapper) {
        this.notificationRepository = notificationRepository;
        this.notificationPublisher = notificationPublisher;
        this.notificationMapper = notificationMapper;
    }

    @Scheduled(cron = "0   18  *   *   *  *")
    public void publishNonUrgentNotifications() {
        log.info("Checking if non-urgent notifications need to be published");
        List<NotificationEntity> notificationEntities = notificationRepository.findByUrgencyAndDateGreaterThanEqualAndCancelledAtIsNull(NotificationUrgency.PUBLISH_WITHIN_24_HOURS, LocalDateTime.now().minusDays(1));
        if (!notificationEntities.isEmpty()) {
            List<Notification> notifications = notificationMapper.mapEntitiesToModel(notificationEntities);
            notificationPublisher.publish(notifications);
        }
    }

}
