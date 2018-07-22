package be.stijnhooft.portal.notifications.dtos;

import be.stijnhooft.portal.model.domain.Event;
import be.stijnhooft.portal.notifications.entities.SubscriptionEntity;
import lombok.*;

/** Combination of a subscription and the event for which it fires **/
@AllArgsConstructor
@Getter
@ToString @EqualsAndHashCode
public class FiringSubscription {

  @NonNull
  private SubscriptionEntity subscription;

  @NonNull
  private Event event;

}
