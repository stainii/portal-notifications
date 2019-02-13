package be.stijnhooft.portal.notifications.entities;

import be.stijnhooft.portal.notifications.model.NotificationUrgency;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "notification")
@ToString
@EqualsAndHashCode
@Getter
@SequenceGenerator(name = "notificationIdGenerator",
    sequenceName = "notification_id_sequence")
@AllArgsConstructor
@NoArgsConstructor
public class NotificationEntity {

    @Id
    @GeneratedValue(generator = "notificationIdGenerator")
    private Long id;

    /**
     * The module that has pushed the notification
     **/
    @NonNull
    private String origin;

    /**
     * An identifier which is the same for correlating events, forming a flow of events.
     * This way, when a "cancel" event is received,
     * older corresponding notifications can looked for and cancelled.
     **/
    @NonNull
    @Column(name = "flow_id")
    private String flowId;

    /**
     * Date and that the notification has been pushed
     **/
    @NonNull
    private LocalDateTime date;

    @NonNull
    private String title;

    @NonNull
    private String message;

    @Embedded
    @NonNull
    private NotificationActionEmbeddable action;

    @Enumerated(EnumType.STRING)
    @NonNull
    private NotificationUrgency urgency;

    @Setter
    private boolean read;

    /**
     * When was this notification cancelled by another notification with the same flowId?
     */
    @Setter
    @Column(name = "cancelled_at")
    private LocalDateTime cancelledAt;

    public boolean isCancelled() {
        return cancelledAt != null;
    }

}
