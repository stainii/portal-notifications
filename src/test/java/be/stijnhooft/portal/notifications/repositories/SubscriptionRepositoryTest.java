package be.stijnhooft.portal.notifications.repositories;

import be.stijnhooft.portal.notifications.PortalNotifications;
import be.stijnhooft.portal.notifications.entities.Subscription;
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
public class SubscriptionRepositoryTest {

  @Inject
  private SubscriptionRepository subscriptionRepository;

  @Test
  @DatabaseSetup("/datasets/SubscriptionRepositoryTest-findByOrigin-initial.xml")
  @DatabaseTearDown("/datasets/clear.xml")
  public void findByOrigin() {
    List<Subscription> subscriptions = subscriptionRepository.findByOrigin("test");
    assertEquals(1, subscriptions.size());
    assertEquals(Long.valueOf(1), subscriptions.get(0).getId());
  }

  @Test
  @DatabaseSetup("/datasets/SubscriptionRepositoryTest-findByOrigin-initial.xml")
  @DatabaseTearDown("/datasets/clear.xml")
  public void findByOriginWhenNothingFound() {
    List<Subscription> subscriptions = subscriptionRepository.findByOrigin("bla");
    assertEquals(0, subscriptions.size());
  }

}
