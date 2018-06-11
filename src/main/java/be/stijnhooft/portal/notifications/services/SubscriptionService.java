package be.stijnhooft.portal.notifications.services;

import be.stijnhooft.portal.model.domain.Event;
import be.stijnhooft.portal.notifications.dtos.FiringSubscription;
import be.stijnhooft.portal.notifications.entities.Subscription;
import be.stijnhooft.portal.notifications.repositories.SubscriptionRepository;
import lombok.NonNull;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;
import java.util.stream.Stream;

@Service
public class SubscriptionService {

    private final ExpressionParser parser = new SpelExpressionParser();
    private final SubscriptionRepository subscriptionRepository;

    @Inject
    public SubscriptionService(SubscriptionRepository subscriptionRepository) {
        this.subscriptionRepository = subscriptionRepository;
    }

    /**
     * When a subscription exists for an event, a firing subscription is returned.
     * If no subscription exists, an empty optional is returned.
     * @param event event
     * @return firing subscription or empty optional
     */
    public Stream<FiringSubscription> fireForEvent(Event event) {
        StandardEvaluationContext context = new StandardEvaluationContext(event);

        return subscriptionRepository.findByOrigin(event.getSource())
                .stream()
                .filter(subscription -> parser.parseExpression(subscription.getActivationCondition()).getValue(context, Boolean.class))
                .map(subscription -> new FiringSubscription(subscription, event));
    }

    public List<Subscription> findAll() {
        return subscriptionRepository.findAll();
    }

    public Subscription createOrUpdate(@NonNull Subscription subscription) {
        return subscriptionRepository.saveAndFlush(subscription);
    }
}
