package be.stijnhooft.portal.notifications.controllers;

import be.stijnhooft.portal.notifications.entities.Subscription;
import be.stijnhooft.portal.notifications.services.SubscriptionService;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.util.List;

@RestController
@RequestMapping("/api/subscription")
public class SubscriptionController {

  private final SubscriptionService subscriptionService;

  @Inject
  public SubscriptionController(SubscriptionService subscriptionService) {
    this.subscriptionService = subscriptionService;
  }

  @RequestMapping("/")
  public List<Subscription> findAll() {
    return subscriptionService.findAll();
  }

  @RequestMapping(method = RequestMethod.POST, path = "/")
  public Subscription create(@RequestBody Subscription subscription) {
    return subscriptionService.createOrUpdate(subscription);
  }

  @RequestMapping(method = RequestMethod.PUT, path = "/{id}")
  public Subscription update(@PathVariable("id") Long id, @RequestBody Subscription subscription) {
    if (!subscription.getId().equals(id)) {
      throw new IllegalArgumentException("Id in subscription json (" + subscription.getId() + ") does not correspond to id in url (" + id +')');
    }
    return subscriptionService.createOrUpdate(subscription);
  }
}
