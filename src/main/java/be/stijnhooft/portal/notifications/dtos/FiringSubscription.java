package be.stijnhooft.portal.notifications.dtos;

import be.stijnhooft.portal.model.domain.Event;
import be.stijnhooft.portal.notifications.entities.Subscription;
import lombok.*;

/** Combination of a subscription and the event for which it fires **/
@AllArgsConstructor
@Getter
@ToString @EqualsAndHashCode
public class FiringSubscription {

  @NonNull
  private Subscription subscription;

  @NonNull
  private Event event;

}
