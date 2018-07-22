package be.stijnhooft.portal.notifications.exceptions;

public class NotificationNotFoundException extends RuntimeException {

    public NotificationNotFoundException(Long id) {
        super("Could not find notification with id " + id);
    }
}
