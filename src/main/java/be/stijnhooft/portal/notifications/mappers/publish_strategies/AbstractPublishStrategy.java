package be.stijnhooft.portal.notifications.mappers.publish_strategies;

import be.stijnhooft.portal.model.notification.PublishStrategy;
import be.stijnhooft.portal.notifications.dtos.FiringSubscription;
import lombok.NonNull;

import java.time.LocalDateTime;

public interface AbstractPublishStrategy {

    PublishStrategy implementedStrategy();

    LocalDateTime apply(@NonNull FiringSubscription firingSubscription);

}
