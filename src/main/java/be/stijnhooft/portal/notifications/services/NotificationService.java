package be.stijnhooft.portal.notifications.services;

import be.stijnhooft.portal.model.domain.Event;
import be.stijnhooft.portal.model.notification.Notification;
import be.stijnhooft.portal.notifications.entities.NotificationEntity;
import be.stijnhooft.portal.notifications.exceptions.NotificationNotFoundException;
import be.stijnhooft.portal.notifications.mappers.NotificationMapper;
import be.stijnhooft.portal.notifications.repositories.NotificationRepository;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Service
@Transactional
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final NotificationMapper notificationMapper;

    @Autowired
    public NotificationService(NotificationRepository notificationRepository, NotificationMapper notificationMapper) {
        this.notificationRepository = notificationRepository;
        this.notificationMapper = notificationMapper;
    }

    public List<Notification> findByRead(boolean read) {
        return map(notificationRepository.findByReadAndCancelledAtIsNullAndPublishedIsTrueOrderByCreatedAtDesc(read));
    }

    public List<Notification> findAll() {
        return map(notificationRepository.findAllByPublishedIsTrueAndCancelledAtIsNullOrderByCreatedAtDesc());
    }

    public void save(Collection<NotificationEntity> notificationEntities) {
        notificationRepository.saveAll(notificationEntities);
    }

    public void cancelNotifications(List<Event> eventsThatCancelNotifications) {
        eventsThatCancelNotifications.forEach(event -> {
            String flowId = event.getFlowId();
            LocalDateTime publishDate = event.getPublishDate();
            notificationRepository.cancelNotificationsWithFlowIdAndBefore(flowId, publishDate);
        });
    }

    public Notification markAsRead(@NonNull Long id, boolean isRead) {
        NotificationEntity notification = notificationRepository.findById(id)
            .orElseThrow(() -> new NotificationNotFoundException(id));

        notification.setRead(isRead);
        return map(notification);
    }

    private Notification map(NotificationEntity notification) {
        return notificationMapper.mapEntityToModel(notification);
    }

    private List<Notification> map(Collection<NotificationEntity> notifications) {
        return notificationMapper.mapEntitiesToModel(notifications);
    }
}
