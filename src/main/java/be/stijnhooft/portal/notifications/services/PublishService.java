package be.stijnhooft.portal.notifications.services;

import be.stijnhooft.portal.notifications.entities.Notification;
import org.apache.camel.CamelContext;

import java.util.Collection;

/** Implementation: see {@link be.stijnhooft.portal.notifications.ModuleConfiguration#createPublishService(CamelContext)} **/
public interface PublishService {

    void publishNotifications(Collection<Notification> notifications);

}
