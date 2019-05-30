package be.stijnhooft.portal.notifications.schedulers;

import be.stijnhooft.portal.notifications.entities.NotificationEntity;
import be.stijnhooft.portal.notifications.mappers.NotificationMapper;
import be.stijnhooft.portal.notifications.messaging.NotificationPublisher;
import be.stijnhooft.portal.notifications.model.Notification;
import be.stijnhooft.portal.notifications.repositories.NotificationRepository;
import be.stijnhooft.portal.notifications.utils.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.util.List;

@Component
@Slf4j
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
        log.info("Checking if notifications need to be published");
        List<NotificationEntity> notificationEntities =
            notificationRepository.findNotificationsThatShouldBePublishedBetween(DateUtils.getCurrentTimeAtZeroSeconds(clock), DateUtils.getCurrentTimePlusOneMinuteAtZeroSeconds(clock));
        if (!notificationEntities.isEmpty()) {
            List<Notification> notifications = notificationMapper.mapEntitiesToModel(notificationEntities);
            notificationPublisher.publish(notifications);
        }
        notificationEntities.forEach(notificationEntity -> notificationEntity.setPublished(true));
    }

}
