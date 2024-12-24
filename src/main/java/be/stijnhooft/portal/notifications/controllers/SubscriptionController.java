package be.stijnhooft.portal.notifications.controllers;

import be.stijnhooft.portal.notifications.entities.SubscriptionEntity;
import be.stijnhooft.portal.notifications.services.SubscriptionService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/subscription")
public class SubscriptionController {

  private final SubscriptionService subscriptionService;

  public SubscriptionController(SubscriptionService subscriptionService) {
    this.subscriptionService = subscriptionService;
  }

  @RequestMapping("/")
  public List<SubscriptionEntity> findAll() {
    return subscriptionService.findAll();
  }

  @PostMapping("/")
  public SubscriptionEntity create(@RequestBody SubscriptionEntity subscription) {
    return subscriptionService.createOrUpdate(subscription);
  }

  @PutMapping("/{id}")
  public SubscriptionEntity update(@PathVariable Long id, @RequestBody SubscriptionEntity subscription) {
    if (!subscription.getId().equals(id)) {
      throw new IllegalArgumentException("Id in subscription json (" + subscription.getId() + ") does not correspond to id in url (" + id +')');
    }
    return subscriptionService.createOrUpdate(subscription);
  }
}
