package be.stijnhooft.portal.notifications.entities;

import be.stijnhooft.portal.model.notification.PublishStrategy;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "notification")
@ToString
@EqualsAndHashCode
@Getter
@SequenceGenerator(name = "notificationIdGenerator",
    sequenceName = "notification_id_sequence")
@AllArgsConstructor
@RequiredArgsConstructor
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

    @NonNull
    private String title;

    @NonNull
    private String message;

    @Embedded
    @NonNull
    private NotificationActionEmbeddable action;

    @Enumerated(EnumType.STRING)
    @NonNull
    @Column(name = "publish_strategy")
    private PublishStrategy publishStrategy;

    /** Date and time that the notification has been created in the database **/
    @NonNull
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    /**
     * Date and time that the notification will need to be shown
     **/
    @NonNull
    @Column(name = "scheduled_at")
    @Setter
    private LocalDateTime scheduledAt;

    /**
     * When was this notification cancelled by another notification with the same flowId?
     */
    @Setter
    @Column(name = "cancelled_at")
    private LocalDateTime cancelledAt;

    /**
     * Has the notification been published?
     */
    @Setter
    @Column(name = "published", nullable = false)
    private boolean published;

    @Setter
    private boolean read;

    public boolean isCancelled() {
        return cancelledAt != null;
    }

}
