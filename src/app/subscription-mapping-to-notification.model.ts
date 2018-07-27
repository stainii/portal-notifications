export class SubscriptionMappingToNotification {

    /** How should the title of the notification be assembled? Should be a Spring EL expression.
     * Example:
     *          'You have an notification from ' + source
     **/
    mappingOfTitle: string;

    /** How should the message of the notification be assembled? Should be a Spring EL expression.
     * Example:
     *          'You have an notification from ' + source + ', containing '+ data['someProperty']
     **/
    mappingOfMessage: string;

    /** Each notification has an action button, triggering an action. How should the text on this button be assembled?
     * Should be a Spring EL expression **/
    mappingOfActionText: string;

    /** Each notification has an action button, triggering an action. What url should be opened when tapping the button?
     * Should be a Spring EL expression **/
    mappingOfActionUrl: string;

}
