package be.stijnhooft.portal.notifications.mappers.publish_strategies;

import be.stijnhooft.portal.notifications.dtos.FiringSubscription;
import be.stijnhooft.portal.notifications.model.PublishStrategy;
import lombok.NonNull;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.time.LocalDateTime;

public interface AbstractPublishStrategy {

    PublishStrategy implementedStrategy();

    LocalDateTime apply(@NonNull FiringSubscription firingSubscription);

}
