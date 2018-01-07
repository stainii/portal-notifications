package be.stijnhooft.portal.notifications.repositories;

import be.stijnhooft.portal.notifications.PortalNotifications;
import be.stijnhooft.portal.notifications.model.Notification;
import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

import javax.inject.Inject;
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

  @Inject
  private NotificationRepository notificationRepository;

  @Test
  @DatabaseSetup("/datasets/NotificationRepositoryTest-findByRead-initial.xml")
  @DatabaseTearDown("/datasets/clear.xml")
  public void findByReadWhenFalse() {
    List<Notification> unreadNotifications = notificationRepository.findByRead(false);
    assertEquals(2, unreadNotifications.size());
    assertEquals(Long.valueOf(1), unreadNotifications.get(0).getId());
    assertEquals(Long.valueOf(3), unreadNotifications.get(1).getId());
  }

  @Test
  @DatabaseSetup("/datasets/NotificationRepositoryTest-findByRead-initial.xml")
  @DatabaseTearDown("/datasets/clear.xml")
  public void findByReadWhenTrue() {
    List<Notification> unreadNotifications = notificationRepository.findByRead(true);
    assertEquals(1, unreadNotifications.size());
    assertEquals(Long.valueOf(2), unreadNotifications.get(0).getId());
  }
}
