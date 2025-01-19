package be.stijnhooft.portal.notifications.mappers;

import be.stijnhooft.portal.model.domain.Event;
import be.stijnhooft.portal.model.notification.Notification;
import be.stijnhooft.portal.model.notification.NotificationAction;
import be.stijnhooft.portal.model.notification.PublishStrategy;
import be.stijnhooft.portal.notifications.dtos.FiringSubscription;
import be.stijnhooft.portal.notifications.entities.NotificationActionEmbeddable;
import be.stijnhooft.portal.notifications.entities.NotificationEntity;
import be.stijnhooft.portal.notifications.entities.SubscriptionMappingToNotificationEmbeddable;
import be.stijnhooft.portal.notifications.mappers.publish_strategies.AbstractPublishStrategy;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationMapper {

    private final ExpressionParser parser = new SpelExpressionParser();

    @Value("${portal-notifications.base-url}")
    private final String baseUrlOfThisDeployment;

    private final List<AbstractPublishStrategy> publishStrategies;

    public NotificationEntity map(@NonNull FiringSubscription firingSubscription) {
        Event event = firingSubscription.getEvent();
        SubscriptionMappingToNotificationEmbeddable mapping = firingSubscription.getSubscription().getMappingToNotification();

        StandardEvaluationContext context = new StandardEvaluationContext(event);
        String title = parser.parseExpression(mapping.ofTitle())
            .getValue(context, String.class);
        String message = parser.parseExpression(mapping.ofMessage())
            .getValue(context, String.class);
        String actionName = parser.parseExpression(mapping.ofActionText())
            .getValue(context, String.class);
        String actionUrl = parser.parseExpression(mapping.ofActionUrl())
            .getValue(context, String.class);
        PublishStrategy publishStrategy = firingSubscription.getSubscription().getPublishStrategy();
        LocalDateTime scheduleDate = determineScheduleDate(firingSubscription);

        log.info("A notification for {} will fire at {}", firingSubscription.getEvent(), scheduleDate);

        NotificationActionEmbeddable action = new NotificationActionEmbeddable(actionUrl, actionName);
        return new NotificationEntity(event.getSource(), event.getFlowId(), title, message, action, publishStrategy, event.getPublishDate(), scheduleDate);
    }

    public be.stijnhooft.portal.model.notification.Notification mapEntityToModel(@NonNull NotificationEntity entity) {
        NotificationAction action =
            new NotificationAction(baseUrlOfThisDeployment + "api/notification/" + entity.getId() + "/action/url/", entity.getAction().getText(), entity.getAction().getInternalUrl());

        return new Notification(entity.getId(), entity.getOrigin(),
            entity.getScheduledAt(), entity.getTitle(), entity.getMessage(), action, entity.getPublishStrategy());
    }

    public List<Notification> mapEntitiesToModel(@NonNull Collection<NotificationEntity> entities) {
        return entities.stream().map(this::mapEntityToModel).collect(Collectors.toList());
    }

    private LocalDateTime determineScheduleDate(FiringSubscription firingSubscription) {
        return
            publishStrategies.stream()
            .filter(strategy -> strategy.implementedStrategy() == firingSubscription.getSubscription().getPublishStrategy())
            .findFirst().orElseThrow(() -> new UnsupportedOperationException("The schedule date cannot be determined for publishStrategy " + firingSubscription.getSubscription().getPublishStrategy()))
            .apply(firingSubscription);
    }

}
