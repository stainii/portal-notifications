package be.stijnhooft.portal.notifications.services;

import be.stijnhooft.portal.model.domain.Event;
import be.stijnhooft.portal.notifications.dtos.FiringSubscription;
import be.stijnhooft.portal.notifications.entities.SubscriptionEntity;
import be.stijnhooft.portal.notifications.repositories.SubscriptionRepository;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

@Service
public class SubscriptionService {

    private final ExpressionParser parser;
    private final SubscriptionRepository subscriptionRepository;

    @Autowired
    public SubscriptionService(SubscriptionRepository subscriptionRepository) {
        this.subscriptionRepository = subscriptionRepository;
        this.parser = new SpelExpressionParser();
    }

    /**
     * When a subscription exists for an event and it's activation condition is met,
     * a firing subscription is returned.
     * If none apply, an empty optional is returned.
     *
     * @param event event
     * @return firing subscription or empty optional
     */
    public Stream<FiringSubscription> fireOnActivationCondition(Event event) {
        return fireOnCondition(event, SubscriptionEntity::getActivationCondition);
    }

    /**
     * When a subscription exists for an event and it's cancellation condition is met,
     * a firing subscription is returned.
     * If none apply, an empty optional is returned.
     *
     * @param event event
     * @return firing subscription or empty optional
     */
    public Stream<FiringSubscription> fireOnCancellationCondition(Event event) {
        return fireOnCondition(event, SubscriptionEntity::getCancellationCondition);
    }

    public List<SubscriptionEntity> findAll() {
        return subscriptionRepository.findAll();
    }

    public SubscriptionEntity createOrUpdate(@NonNull SubscriptionEntity subscription) {
        return subscriptionRepository.saveAndFlush(subscription);
    }

    private Stream<FiringSubscription> fireOnCondition(Event event, Function<SubscriptionEntity, String> condition) {
        StandardEvaluationContext context = new StandardEvaluationContext(event);
        return subscriptionRepository.findByOrigin(event.getSource())
            .stream()
            .filter(subscription -> parser.parseExpression(condition.apply(subscription)).getValue(context, Boolean.class))
            .map(subscription -> new FiringSubscription(subscription, event));
    }

}
