package be.stijnhooft.portal.notifications;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class PortalNotifications {

    public static void main(String[] args) {
        SpringApplication.run(PortalNotifications.class, args);
    }

}
