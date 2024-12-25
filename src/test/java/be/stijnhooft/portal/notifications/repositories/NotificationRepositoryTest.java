package be.stijnhooft.portal.notifications.repositories;

import be.stijnhooft.portal.notifications.PortalNotifications;
import be.stijnhooft.portal.notifications.entities.NotificationEntity;
import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.springtestdbunit.assertion.DatabaseAssertionMode;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest(classes = PortalNotifications.class)
@ActiveProfiles("local")
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
    DirtiesContextTestExecutionListener.class,
    TransactionalTestExecutionListener.class,
    DbUnitTestExecutionListener.class})
public class NotificationRepositoryTest {

    @Autowired
    private NotificationRepository notificationRepository;

    @Test
    @DatabaseSetup("/datasets/NotificationRepositoryTest-findByReadAndPublishedIsTrueOrderByCreatedAtDesc-initial.xml")
    @DatabaseTearDown("/datasets/clear.xml")
    public void findByReadAndCancelledAtIsNullAndPublishedIsTrueOrderByDateDescWhenFalse() {
        List<NotificationEntity> unreadNotifications = notificationRepository.findByReadAndCancelledAtIsNullAndPublishedIsTrueOrderByCreatedAtDesc(false);
        assertEquals(2, unreadNotifications.size());
        assertEquals(Long.valueOf(3), unreadNotifications.getFirst().getId());
        assertEquals(Long.valueOf(1), unreadNotifications.get(1).getId());
    }

    @Test
    @DatabaseSetup("/datasets/NotificationRepositoryTest-findByReadAndPublishedIsTrueOrderByCreatedAtDesc-initial.xml")
    @DatabaseTearDown("/datasets/clear.xml")
    public void findByReadAndCancelledAtIsNullAndPublishedIsTrueOrderByDateDescWhenTrue() {
        List<NotificationEntity> unreadNotifications = notificationRepository.findByReadAndCancelledAtIsNullAndPublishedIsTrueOrderByCreatedAtDesc(true);
        assertEquals(1, unreadNotifications.size());
        assertEquals(Long.valueOf(2), unreadNotifications.getFirst().getId());
    }

    @Test
    @DatabaseSetup("/datasets/NotificationRepositoryTest-findAllByPublishedIsTrueAndCancelledAtIsNullOrderByCreatedAtDesc-initial.xml")
    @DatabaseTearDown("/datasets/clear.xml")
    public void findAllByPublishedIsTrueAndCancelledAtIsNullOrderByDateDesc() {
        List<NotificationEntity> unreadNotifications = notificationRepository.findAllByPublishedIsTrueAndCancelledAtIsNullOrderByCreatedAtDesc();
        assertEquals(3, unreadNotifications.size());
        assertEquals(Long.valueOf(3), unreadNotifications.getFirst().getId());
        assertEquals(Long.valueOf(2), unreadNotifications.get(1).getId());
        assertEquals(Long.valueOf(1), unreadNotifications.get(2).getId());
    }

    @Test
    @DatabaseSetup("/datasets/NotificationRepositoryTest-findNotificationsThatShouldBePublishedBetween-initial.xml")
    @DatabaseTearDown("/datasets/clear.xml")
    public void findNotificationsThatShouldBePublishedBetween() {
        LocalDateTime max = LocalDateTime.of(2019, 5, 30, 7, 36, 0);
        LocalDateTime min = LocalDateTime.of(2019, 5, 30, 7, 35, 0);

        List<NotificationEntity> unpublishedNotificationsThatShouldBePublished = notificationRepository.findNotificationsThatShouldBePublishedBetween(min, max);

        assertEquals(1, unpublishedNotificationsThatShouldBePublished.size());
        assertEquals(Long.valueOf(2), unpublishedNotificationsThatShouldBePublished.getFirst().getId());
    }

    @Test
    @DatabaseSetup("/datasets/NotificationRepositoryTest-cancelNotificationsWithFlowIdAndBefore-initial.xml")
    @ExpectedDatabase(value = "/datasets/NotificationRepositoryTest-cancelNotificationsWithFlowIdAndBefore-expected.xml", assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED)
    @DatabaseTearDown("/datasets/clear.xml")
    @Transactional
    public void cancelNotificationsWithFlowIdAndBefore() {
        notificationRepository.cancelNotificationsWithFlowIdAndBefore("abc", LocalDateTime.of(2018, Month.OCTOBER, 23, 10, 8));
    }

}
