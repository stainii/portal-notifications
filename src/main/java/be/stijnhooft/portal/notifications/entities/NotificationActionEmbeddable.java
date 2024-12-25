package be.stijnhooft.portal.notifications.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

@ToString
@EqualsAndHashCode
@Getter
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class NotificationActionEmbeddable {

    /**
     * url where the user actually needs to be forwarded to, when he/she taps on the notification.
     * This is another url than the "url" of the NotificationAction of the model.
     **/
    @Column(name = "action_url")
    private String internalUrl;

    /**
     * the text that should appear on the action button of the notification
     **/
    @Column(name = "action_text")
    private String text;

}
