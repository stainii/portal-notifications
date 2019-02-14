package be.stijnhooft.portal.notifications.repositories;

import be.stijnhooft.portal.notifications.PortalNotifications;
import be.stijnhooft.portal.notifications.entities.NotificationEntity;
import be.stijnhooft.portal.notifications.model.NotificationUrgency;
import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.springtestdbunit.assertion.DatabaseAssertionMode;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

import static org.junit.Assert.assertEquals;


@RunWith(SpringRunner.class)
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
    @DatabaseSetup("/datasets/NotificationRepositoryTest-findByReadOrderByDateDesc-initial.xml")
    @DatabaseTearDown("/datasets/clear.xml")
    public void findByReadAndCancelledAtIsNullOrderByDateDescWhenFalse() {
        List<NotificationEntity> unreadNotifications = notificationRepository.findByReadAndCancelledAtIsNullOrderByDateDesc(false);
        assertEquals(2, unreadNotifications.size());
        assertEquals(Long.valueOf(3), unreadNotifications.get(0).getId());
        assertEquals(Long.valueOf(1), unreadNotifications.get(1).getId());
    }

    @Test
    @DatabaseSetup("/datasets/NotificationRepositoryTest-findByReadOrderByDateDesc-initial.xml")
    @DatabaseTearDown("/datasets/clear.xml")
    public void findByReadAndCancelledAtIsNullOrderByDateDescWhenTrue() {
        List<NotificationEntity> unreadNotifications = notificationRepository.findByReadAndCancelledAtIsNullOrderByDateDesc(true);
        assertEquals(1, unreadNotifications.size());
        assertEquals(Long.valueOf(2), unreadNotifications.get(0).getId());
    }

    @Test
    @DatabaseSetup("/datasets/NotificationRepositoryTest-findByUrgency-initial.xml")
    @DatabaseTearDown("/datasets/clear.xml")
    public void findByUrgencyAndDateGreaterThanEqualAndCancelledAtIsNullWhenPublishImmediately() {
        List<NotificationEntity> unreadNotifications = notificationRepository.findByUrgencyAndDateGreaterThanEqualAndCancelledAtIsNull(NotificationUrgency.PUBLISH_IMMEDIATELY, LocalDateTime.of(2018, 4, 22, 18, 0));
        assertEquals(1, unreadNotifications.size());
        assertEquals(Long.valueOf(1), unreadNotifications.get(0).getId());
    }

    @Test
    @DatabaseSetup("/datasets/NotificationRepositoryTest-findByUrgency-initial.xml")
    @DatabaseTearDown("/datasets/clear.xml")
    public void findByUrgencyAndDateGreaterThanEqualAndCancelledAtIsNullWhenPublishWithin24Hours() {
        List<NotificationEntity> unreadNotifications = notificationRepository.findByUrgencyAndDateGreaterThanEqualAndCancelledAtIsNull(NotificationUrgency.PUBLISH_WITHIN_24_HOURS, LocalDateTime.of(2018, 4, 22, 18, 0));
        assertEquals(1, unreadNotifications.size());
        assertEquals(Long.valueOf(2), unreadNotifications.get(0).getId());
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
