package be.stijnhooft.portal.notifications.controllers;

import be.stijnhooft.portal.model.notification.Notification;
import be.stijnhooft.portal.notifications.dtos.NotificationReadStatus;
import be.stijnhooft.portal.notifications.exceptions.NotificationNotFoundException;
import be.stijnhooft.portal.notifications.services.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/notification")
public class NotificationController {

    private final NotificationService notificationService;

    @Autowired
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

    @RequestMapping(method = RequestMethod.PUT, path = "/{id}/read")
    public ResponseEntity<Notification> markAsRead(@PathVariable("id") Long id, @RequestBody NotificationReadStatus readStatus) {
        if (id.longValue() != readStatus.getId().longValue()) {
            throw new IllegalArgumentException("The id in the url (" + id + ") is not the same as the id in the payload (" + readStatus.getId() + ")");
        }
        try {
            Notification updatedNotification = notificationService.markAsRead(id, readStatus.getRead());
            return ResponseEntity.ok(updatedNotification);
        } catch (NotificationNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * When the user receives a notification, he/she will also have the option to click on an action button.
     * This action button opens an url.
     *
     * Instead of going directly to the url, the front-end is expected to point to this REST method.
     * By going to this REST method, the notification will be marked as read,
     * before redirecting the user to the actual url.
     */
    @RequestMapping(method = RequestMethod.GET, path = "/{id}/action/url")
    public void markAsReadAndRedirectToActionUrl(@PathVariable("id") Long id, HttpServletResponse response) throws IOException {
        try {
            Notification notification = notificationService.markAsRead(id, true);
            response.sendRedirect(notification.getAction().getInternalUrl());
        } catch (NotificationNotFoundException ex) {
            response.setStatus(HttpStatus.NOT_FOUND.value());
        }
    }
}
