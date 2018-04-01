package be.stijnhooft.portal.notifications.services;

import be.stijnhooft.portal.model.domain.Event;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

@Service
@Transactional
@Slf4j
public class EventService {

  public void receiveEvents(Collection<Event> events) {
    log.error("Received events: " + events);
  }

}
