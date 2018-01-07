package be.stijnhooft.portal.notifications.controllers;

import be.stijnhooft.portal.notifications.model.Notification;
import be.stijnhooft.portal.notifications.services.NotificationService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import java.util.List;

@RestController
@RequestMapping("/api/notification")
public class NotificationController {

  private final NotificationService notificationService;

  @Inject
  public NotificationController(NotificationService notificationService) {
    this.notificationService = notificationService;
  }

  @RequestMapping("/")
  public List<Notification> find(@RequestParam(required = false) Boolean onlyUnread) {
    if (onlyUnread != null) {
      return notificationService.findByRead(!onlyUnread);
    } else {
      return notificationService.findAll();
    }
  }
}
