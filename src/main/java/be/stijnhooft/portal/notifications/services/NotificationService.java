package be.stijnhooft.portal.notifications.services;

import be.stijnhooft.portal.notifications.entities.Notification;
import be.stijnhooft.portal.notifications.messaging.NotificationPublisher;
import be.stijnhooft.portal.notifications.model.NotificationUrgency;
import be.stijnhooft.portal.notifications.repositories.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final NotificationPublisher notificationPublisher;

    @Autowired
    public NotificationService(NotificationRepository notificationRepository, NotificationPublisher notificationPublisher) {
        this.notificationRepository = notificationRepository;
        this.notificationPublisher = notificationPublisher;
    }

    public List<Notification> findByRead(boolean read) {
        return notificationRepository.findByRead(read);
    }

    public List<Notification> findAll() {
        return notificationRepository.findAll();
    }

    public Collection<Notification> saveAndIfUrgentThenPublish(Collection<Notification> notifications) {
        notifications.forEach(notificationRepository::save);
        publishUrgentNotifications(notifications);
        //the other, non-urgent notifications, will be published by a scheduled method

        return notifications;
    }

    private void publishUrgentNotifications(Collection<Notification> notifications) {
        List<Notification> urgentNotifications = notifications.stream()
            .filter(notification -> notification.getUrgency() == NotificationUrgency.PUBLISH_IMMEDIATELY)
            .collect(Collectors.toList());
        notificationPublisher.publish(urgentNotifications);
    }
}
