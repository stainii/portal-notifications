package be.stijnhooft.portal.notifications.mappers.publish_strategies;

import be.stijnhooft.portal.notifications.dtos.FiringSubscription;
import be.stijnhooft.portal.notifications.model.PublishStrategy;
import lombok.NonNull;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class PublishImmediatelyPublishStrategy implements AbstractPublishStrategy {

    @Override
    public PublishStrategy implementedStrategy() {
        return PublishStrategy.PUBLISH_IMMEDIATELY;
    }

    @Override
    public LocalDateTime apply(@NonNull FiringSubscription firingSubscription) {
        return LocalDateTime.now();
    }
}
