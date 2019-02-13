package be.stijnhooft.portal.notifications.repositories;

import be.stijnhooft.portal.notifications.entities.NotificationEntity;
import be.stijnhooft.portal.notifications.model.NotificationUrgency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<NotificationEntity, Long> {

    List<NotificationEntity> findByReadAndCancelledAtIsNullOrderByDateDesc(boolean read);

    List<NotificationEntity> findByUrgencyAndDateGreaterThanEqualAndCancelledAtIsNull(NotificationUrgency urgency, LocalDateTime dateTime);

    @Modifying
    @Query("update NotificationEntity set cancelledAt = :publishDate where flowId = :flowId and date < :publishDate")
    void cancelNotificationsWithFlowIdAndBefore(String flowId, LocalDateTime publishDate);

}
