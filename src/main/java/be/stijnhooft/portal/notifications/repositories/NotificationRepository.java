package be.stijnhooft.portal.notifications.repositories;

import be.stijnhooft.portal.notifications.entities.NotificationEntity;
import be.stijnhooft.portal.notifications.model.PublishStrategy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<NotificationEntity, Long> {

    List<NotificationEntity> findByReadAndCancelledAtIsNullAndPublishedIsTrueOrderByCreatedAtDesc(boolean read);

    List<NotificationEntity> findAllByPublishedIsTrueAndCancelledAtIsNullOrderByCreatedAtDesc();

    @Query("select n from NotificationEntity n " +
        "where n.cancelledAt is null " +
        "and n.published = false " +
        "and n.scheduledAt >= :scheduledAfter " +
        "and n.scheduledAt <= :scheduledBefore")
    List<NotificationEntity> findNotificationsThatShouldBePublishedBetween(LocalDateTime scheduledAfter, LocalDateTime scheduledBefore);

    @Modifying
    @Query("update NotificationEntity set cancelledAt = :publishDate where flowId = :flowId and createdAt < :publishDate")
    void cancelNotificationsWithFlowIdAndBefore(String flowId, LocalDateTime publishDate);

    @Query("select max(n.scheduledAt) from NotificationEntity n where n.published = true")
    LocalDateTime findLastPublishDate();
}
