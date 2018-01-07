package be.stijnhooft.portal.notifications.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@ToString @EqualsAndHashCode
@Getter
@Embeddable
public class NotificationAction {

  /** url that needs to be opened when the user taps the notification **/
  @Column(name = "action_url")
  private String url;

  /** the text that should appear on the action button of the notification **/
  @Column(name = "action_text")
  private String text;

}
