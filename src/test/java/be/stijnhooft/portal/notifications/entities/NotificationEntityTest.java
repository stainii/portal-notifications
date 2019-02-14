package be.stijnhooft.portal.notifications.entities;

import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class NotificationEntityTest {

    private NotificationEntity notification;

    @Before
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
