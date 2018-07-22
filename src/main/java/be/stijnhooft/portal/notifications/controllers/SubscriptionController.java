package be.stijnhooft.portal.notifications.controllers;

import be.stijnhooft.portal.notifications.entities.SubscriptionEntity;
import be.stijnhooft.portal.notifications.services.SubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/subscription")
public class SubscriptionController {

  private final SubscriptionService subscriptionService;

  @Autowired
  public SubscriptionController(SubscriptionService subscriptionService) {
    this.subscriptionService = subscriptionService;
  }

  @RequestMapping("/")
  public List<SubscriptionEntity> findAll() {
    return subscriptionService.findAll();
  }

  @RequestMapping(method = RequestMethod.POST, path = "/")
  public SubscriptionEntity create(@RequestBody SubscriptionEntity subscription) {
    return subscriptionService.createOrUpdate(subscription);
  }

  @RequestMapping(method = RequestMethod.PUT, path = "/{id}")
  public SubscriptionEntity update(@PathVariable("id") Long id, @RequestBody SubscriptionEntity subscription) {
    if (!subscription.getId().equals(id)) {
      throw new IllegalArgumentException("Id in subscription json (" + subscription.getId() + ") does not correspond to id in url (" + id +')');
    }
    return subscriptionService.createOrUpdate(subscription);
  }
}
