package be.stijnhooft.portal.notifications.services;

import be.stijnhooft.portal.notifications.entities.NotificationEntity;
import be.stijnhooft.portal.notifications.exceptions.NotificationNotFoundException;
import be.stijnhooft.portal.notifications.mappers.NotificationMapper;
import be.stijnhooft.portal.notifications.messaging.NotificationPublisher;
import be.stijnhooft.portal.notifications.model.Notification;
import be.stijnhooft.portal.notifications.model.NotificationUrgency;
import be.stijnhooft.portal.notifications.repositories.NotificationRepository;
import lombok.NonNull;
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
    private final NotificationMapper notificationMapper;

    @Autowired
    public NotificationService(NotificationRepository notificationRepository, NotificationPublisher notificationPublisher, NotificationMapper notificationMapper) {
        this.notificationRepository = notificationRepository;
        this.notificationPublisher = notificationPublisher;
        this.notificationMapper = notificationMapper;
    }

    public List<Notification> findByRead(boolean read) {
        return map(notificationRepository.findByReadOrderByDateDesc(read));
    }

    public List<Notification> findAll() {
        return map(notificationRepository.findAll());
    }

    public Collection<Notification> saveAndIfUrgentThenPublish(Collection<NotificationEntity> notificationEntities) {
        notificationEntities.forEach(notificationRepository::save);
        notificationRepository.flush(); //flush, to make sure we know the ids of the entities, so that the public action can be generated

        Collection<Notification> notifications = map(notificationEntities);
        publishUrgentNotifications(notifications);
        //the other, non-urgent notifications, will be published by a scheduled method

        return notifications;
    }

    public Notification markAsRead(@NonNull Long id, boolean isRead) {
        NotificationEntity notification = notificationRepository.findById(id)
            .orElseThrow(() -> new NotificationNotFoundException(id));

        notification.setRead(isRead);
        return map(notification);
    }

    private void publishUrgentNotifications(Collection<Notification> notifications) {
        List<Notification> urgentNotifications = notifications.stream()
            .filter(notification -> notification.getUrgency() == NotificationUrgency.PUBLISH_IMMEDIATELY)
            .collect(Collectors.toList());
        notificationPublisher.publish(urgentNotifications);
    }

    private Notification map(NotificationEntity notification) {
        return notificationMapper.mapEntityToModel(notification);
    }

    private List<Notification> map(Collection<NotificationEntity> notifications) {
        return notificationMapper.mapEntitiesToModel(notifications);
    }

}
