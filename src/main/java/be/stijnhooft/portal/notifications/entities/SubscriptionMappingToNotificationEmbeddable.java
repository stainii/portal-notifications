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
public class SubscriptionMappingToNotificationEmbeddable {


    /**
     * How should the title of the notification be assembled? Should be a Spring EL expression.
     * Example:
     * 'You have an notification from ' + source
     **/
    @Getter
    @NonNull
    @Column(name = "mapping_of_title", nullable = false)
    private String mappingOfTitle;

    /**
     * How should the message of the notification be assembled? Should be a Spring EL expression.
     * Example:
     * 'You have an notification from ' + source + ', containing '+ data['someProperty']
     **/
    @Getter
    @NonNull
    @Column(name = "mapping_of_message", nullable = false)
    private String mappingOfMessage;

    /**
     * Each notification has an action button, triggering an action. How should the text on this button be assembled?
     * Should be a Spring EL expression
     **/
    @Getter
    @NonNull
    @Column(name = "mapping_of_action_text", nullable = false)
    private String mappingOfActionText;

    /**
     * Each notification has an action button, triggering an action. What url should be opened when tapping the button?
     * Should be a Spring EL expression
     **/
    @Getter
    @NonNull
    @Column(name = "mapping_of_action_url", nullable = false)
    private String mappingOfActionUrl;

    /**
     * When should the notification be pushed to the user? Format: ISO-8601 (YYYY-MM-DDThh:mm:ss)
     * Should be a Spring EL expression
     **/
    @Getter
    @Column(name = "mapping_of_schedule_date_time")
    private String mappingOfScheduleDateTime;

    /**
     * Alternative to getter, to make code more concise. Meant to be used as: mapping.ofTitle();
     **/
    public String ofTitle() {
        return getMappingOfTitle();
    }

    /**
     * Alternative to getter, to make code more concise. Meant to be used as: mapping.ofMessage();
     **/
    public String ofMessage() {
        return getMappingOfMessage();
    }

    /**
     * Alternative to getter, to make code more concise. Meant to be used as: mapping.ofActionText();
     **/
    public String ofActionText() {
        return getMappingOfActionText();
    }

    /**
     * Alternative to getter, to make code more concise. Meant to be used as: mapping.ofActionUrl();
     **/
    public String ofActionUrl() {
        return getMappingOfActionUrl();
    }

    /**
     * Alternative to getter, to make code more concise. Meant to be used as: mapping.ofScheduleDateTime();
     **/
    public String ofScheduleDateTime() {
        return getMappingOfScheduleDateTime();
    }

}
