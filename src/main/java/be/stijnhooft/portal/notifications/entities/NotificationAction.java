package be.stijnhooft.portal.notifications.entities;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@ToString @EqualsAndHashCode
@Getter
@Embeddable
@AllArgsConstructor @NoArgsConstructor
public class NotificationAction {

  /** url that needs to be opened when the user taps the notification **/
  @Column(name = "action_url")
  private String url;

  /** the text that should appear on the action button of the notification **/
  @Column(name = "action_text")
  private String text;

}
