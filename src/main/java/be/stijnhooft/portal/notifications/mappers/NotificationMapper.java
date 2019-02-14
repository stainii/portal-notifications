package be.stijnhooft.portal.notifications.mappers;

import be.stijnhooft.portal.model.domain.Event;
import be.stijnhooft.portal.notifications.dtos.FiringSubscription;
import be.stijnhooft.portal.notifications.entities.NotificationActionEmbeddable;
import be.stijnhooft.portal.notifications.entities.NotificationEntity;
import be.stijnhooft.portal.notifications.entities.SubscriptionMappingToNotificationEmbeddable;
import be.stijnhooft.portal.notifications.model.Notification;
import be.stijnhooft.portal.notifications.model.NotificationAction;
import be.stijnhooft.portal.notifications.model.NotificationUrgency;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class NotificationMapper {

    private final ExpressionParser parser = new SpelExpressionParser();

    @Value("${baseUrl}")
    private String baseUrlOfThisDeployment;

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
        NotificationUrgency urgency = firingSubscription.getSubscription().getUrgency();

        NotificationActionEmbeddable action = new NotificationActionEmbeddable(actionUrl, actionName);
        return new NotificationEntity(null, event.getSource(), event.getFlowId(), event.getPublishDate(), title, message, action, urgency, false, null);
    }

    public be.stijnhooft.portal.notifications.model.Notification mapEntityToModel(@NonNull NotificationEntity entity) {
        NotificationAction action =
            new NotificationAction(baseUrlOfThisDeployment + "api/notification/" + entity.getId() + "/action/url/", entity.getAction().getText(), entity.getAction().getInternalUrl());

        return new Notification(entity.getId(), entity.getOrigin(),
            entity.getDate(), entity.getTitle(), entity.getMessage(), action, entity.getUrgency());
    }

    public List<Notification> mapEntitiesToModel(@NonNull Collection<NotificationEntity> entities) {
        return entities.stream().map(this::mapEntityToModel).collect(Collectors.toList());
    }


}
