package be.stijnhooft.portal.notifications.entities;

import lombok.*;

import javax.persistence.Embeddable;

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
    private String mappingOfTitle;

    /**
     * How should the message of the notification be assembled? Should be a Spring EL expression.
     * Example:
     * 'You have an notification from ' + source + ', containing '+ data['someProperty']
     **/
    @Getter
    @NonNull
    private String mappingOfMessage;

    /**
     * Each notification has an action button, triggering an action. How should the text on this button be assembled?
     * Should be a Spring EL expression
     **/
    @Getter
    @NonNull
    private String mappingOfActionText;

    /**
     * Each notification has an action button, triggering an action. What url should be opened when tapping the button?
     * Should be a Spring EL expression
     **/
    @Getter
    @NonNull
    private String mappingOfActionUrl;

    /**
     * When should the notification be pushed to the user? Format: ISO-8601 (YYYY-MM-DDThh:mm:ss)
     * Should be a Spring EL expression
     **/
    @Getter
    @NonNull
    private String mappingOfScheduleDate;

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
     * Alternative to getter, to make code more concise. Meant to be used as: mapping.ofScheduleDate();
     **/
    public String ofScheduleDate() {
        return getMappingOfScheduleDate();
    }

}
