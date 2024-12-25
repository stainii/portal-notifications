package be.stijnhooft.portal.notifications.mappers.publish_strategies;

import be.stijnhooft.portal.model.notification.PublishStrategy;
import be.stijnhooft.portal.notifications.dtos.FiringSubscription;
import be.stijnhooft.portal.notifications.entities.NotificationEntity;
import be.stijnhooft.portal.notifications.repositories.NotificationRepository;
import be.stijnhooft.portal.notifications.utils.DateUtils;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;

/**
 * When using this strategy, we try to to bundle several notifications as much as possible.
 * If another notification is planned within 24 hours, both notifications will trigger simultaneously,
 * disturbing the user as less as possible.
 */
@Slf4j
@Component
public class PublishWithin24HoursPublishStrategy implements AbstractPublishStrategy {

    private final NotificationRepository notificationRepository;
    private final Clock clock;

    public PublishWithin24HoursPublishStrategy(NotificationRepository notificationRepository, Clock clock) {
        this.notificationRepository = notificationRepository;
        this.clock = clock;
    }

    @Override
    public PublishStrategy implementedStrategy() {
        return PublishStrategy.PUBLISH_WITHIN_24_HOURS;
    }

    @Override
    public LocalDateTime apply(@NonNull FiringSubscription firingSubscription) {
        List<NotificationEntity> notificationsThatArePlannedWithinTheNext24Hours = notificationRepository.findNotificationsThatShouldBePublishedBetween(DateUtils.now(clock), DateUtils.tomorrow(clock));

        if (notificationsThatArePlannedWithinTheNext24Hours.isEmpty()) {
            return nextTimeItIs1600();
        } else {
            return notificationsThatArePlannedWithinTheNext24Hours.getFirst().getScheduledAt();
        }
    }

    private LocalDateTime nextTimeItIs1600() {
        if (DateUtils.now(clock).getHour() >= 16) {
            return DateUtils.now(clock)
                .plusDays(1)
                .withHour(16)
                .withMinute(0)
                .withSecond(0);
        } else {
            return DateUtils.now(clock)
                .withHour(16)
                .withMinute(0)
                .withSecond(0);
        }
    }


}
