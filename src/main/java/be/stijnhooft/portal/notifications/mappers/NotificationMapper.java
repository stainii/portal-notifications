package be.stijnhooft.portal.notifications.mappers;

import be.stijnhooft.portal.model.domain.Event;
import be.stijnhooft.portal.notifications.dtos.FiringSubscription;
import be.stijnhooft.portal.notifications.entities.Notification;
import be.stijnhooft.portal.notifications.entities.NotificationAction;
import be.stijnhooft.portal.notifications.entities.SubscriptionMappingToNotification;
import be.stijnhooft.portal.notifications.model.NotificationUrgency;
import lombok.NonNull;
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

    public Notification map(@NonNull FiringSubscription firingSubscription) {
        Event event = firingSubscription.getEvent();
        SubscriptionMappingToNotification mapping = firingSubscription.getSubscription().getMappingToNotification();

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

        NotificationAction action = new NotificationAction(actionUrl, actionName);
        return new Notification(null, event.getSource(), event.getPublishDate(), title, message, action, urgency, false);
    }

    public be.stijnhooft.portal.notifications.model.Notification mapEntityToModel(@NonNull Notification entity) {
        be.stijnhooft.portal.notifications.model.NotificationAction action =
            new be.stijnhooft.portal.notifications.model.NotificationAction(entity.getAction().getUrl(), entity.getAction().getText());

        return new be.stijnhooft.portal.notifications.model.Notification(entity.getId(), entity.getOrigin(),
            entity.getDate(), entity.getTitle(), entity.getMessage(), entity.getUrgency(), action);
    }

    public List<be.stijnhooft.portal.notifications.model.Notification> mapEntitiesToModel(@NonNull Collection<Notification> entities) {
        return entities.stream().map(this::mapEntityToModel).collect(Collectors.toList());
    }


}
