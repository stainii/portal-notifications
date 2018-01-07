package be.stijnhooft.portal.notifications.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@ToString @EqualsAndHashCode
@Getter
@SequenceGenerator( name = "notificationIdGenerator",
  sequenceName = "notification_id_sequence")
public class Notification {

  @Id
  @GeneratedValue(generator = "notificationIdGenerator")
  private Long id;

  /** The module that has pushed the notification **/
  @NonNull
  private String origin;

  /** Date and that the notification has been pushed **/
  @NonNull
  private LocalDateTime date;

  @NonNull
  private String title;

  @NonNull
  private String message;

  @Embedded
  private NotificationAction action;

  private boolean read;

}
