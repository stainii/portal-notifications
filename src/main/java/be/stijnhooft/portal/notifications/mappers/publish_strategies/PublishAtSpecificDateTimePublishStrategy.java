package be.stijnhooft.portal.notifications.mappers.publish_strategies;

import be.stijnhooft.portal.model.domain.Event;
import be.stijnhooft.portal.model.notification.PublishStrategy;
import be.stijnhooft.portal.notifications.dtos.FiringSubscription;
import be.stijnhooft.portal.notifications.entities.SubscriptionMappingToNotificationEmbeddable;
import lombok.NonNull;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

import static org.springframework.util.ObjectUtils.isEmpty;

@Component
public class PublishAtSpecificDateTimePublishStrategy implements AbstractPublishStrategy {

    @Override
    public PublishStrategy implementedStrategy() {
        return PublishStrategy.PUBLISH_AT_SPECIFIC_DATE_TIME;
    }

    @Override
    public LocalDateTime apply(@NonNull FiringSubscription firingSubscription) {
        Event event = firingSubscription.getEvent();
        SubscriptionMappingToNotificationEmbeddable mapping = firingSubscription.getSubscription().getMappingToNotification();

        if (isEmpty(mapping.ofScheduleDateTime())) {
            throw new IllegalArgumentException("When using the strategy 'PUBLISH_AT_A_SPECIFIC_DATE_TIME', it is required to provide a mapping to determine this specific date time.");
        }

        ExpressionParser parser = new SpelExpressionParser();
        StandardEvaluationContext context = new StandardEvaluationContext(event);

        String scheduleDateAsString = parser.parseExpression(mapping.ofScheduleDateTime())
            .getValue(context, String.class);

        return LocalDateTime.parse(scheduleDateAsString);
    }
}
