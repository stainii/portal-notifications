package be.stijnhooft.portal.notifications.entities;

import be.stijnhooft.portal.notifications.model.NotificationUrgency;
import lombok.*;

import javax.persistence.*;

@ToString @EqualsAndHashCode
@Getter
@Entity
@AllArgsConstructor @NoArgsConstructor
@SequenceGenerator( name = "subscriptionIdGenerator",
  sequenceName = "subscription_id_sequence")
public class Subscription {

  @Id
  @GeneratedValue(generator = "subscriptionIdGenerator")
  private Long id;

  /** The origin that sends an data event **/
  @Getter
  @NonNull
  private String origin;

  /** To which conditions should the data event apply to fire this subscription. Should be a Spring EL expression.
   * Examples:
   *          true (always)
   *          data['someProperty'] == "bla"
   **/
  @Getter
  @NonNull
  @Column(name = "activation_condition")
  private String activationCondition;

  @Getter
  @NonNull
  @Embedded
  private SubscriptionMappingToNotification mappingToNotification;

  @Getter
  @NonNull
  @Enumerated(EnumType.STRING)
  private NotificationUrgency urgency;

}
