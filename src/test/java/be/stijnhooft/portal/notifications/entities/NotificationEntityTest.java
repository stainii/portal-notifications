package be.stijnhooft.portal.notifications.entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class NotificationEntityTest {

    private NotificationEntity notification;

    @BeforeEach
    public void setUp() {
        notification = new NotificationEntity();
    }

    @Test
    public void isCancelledWhenTrue() {
        notification.setCancelledAt(LocalDateTime.now());
        assertTrue(notification.isCancelled());
    }

    @Test
    public void isCancelledWhenFalse() {
        notification.setCancelledAt(null);
        assertFalse(notification.isCancelled());
    }

}
