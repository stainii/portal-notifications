package be.stijnhooft.portal.notifications.entities;

import be.stijnhooft.portal.notifications.model.NotificationUrgency;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@ToString @EqualsAndHashCode
@Getter
@SequenceGenerator( name = "notificationIdGenerator",
  sequenceName = "notification_id_sequence")
@AllArgsConstructor @NoArgsConstructor
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
  @NonNull
  private NotificationAction action;

  @Enumerated(EnumType.STRING)
  @NonNull
  private NotificationUrgency urgency;

  private boolean read;

}
