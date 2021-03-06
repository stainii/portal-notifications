package be.stijnhooft.portal.notifications.repositories;

import be.stijnhooft.portal.notifications.entities.SubscriptionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubscriptionRepository extends JpaRepository<SubscriptionEntity, Long> {

  List<SubscriptionEntity> findByOrigin(String origin);

}
