package be.stijnhooft.portal.notifications;

import be.stijnhooft.portal.model.domain.Event;
import be.stijnhooft.portal.model.domain.FlowAction;
import be.stijnhooft.portal.notifications.entities.SubscriptionEntity;
import be.stijnhooft.portal.notifications.entities.SubscriptionMappingToNotificationEmbeddable;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dasniko.testcontainers.keycloak.KeycloakContainer;
import lombok.SneakyThrows;
import org.apache.http.client.utils.URIBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import static be.stijnhooft.portal.model.notification.PublishStrategy.PUBLISH_IMMEDIATELY;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Testcontainers
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class EventToNotificationIntegrationTest {

    @Container
    @ServiceConnection
    static RabbitMQContainer rabbitMQContainer = new RabbitMQContainer("rabbitmq:3-management")
        .withReuse(true);

    @Container
    static KeycloakContainer keycloakContainer = new KeycloakContainer("quay.io/keycloak/keycloak:26.1")
        .withRealmImportFile("/realm-export.json");

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.cloud.stream.binders.rabbit.environment.spring.rabbitmq.port", rabbitMQContainer::getAmqpPort);
        registry.add("spring.cloud.stream.binders.rabbit.environment.spring.rabbitmq.host", rabbitMQContainer::getHost);
        registry.add("spring.security.oauth2.resourceserver.jwt.issuer-uri", () -> keycloakContainer.getAuthServerUrl() + "/realms/portal-realm");
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private NotificationsListener notificationsListener;

    @BeforeEach
    void setUp() {
        notificationsListener.reset();
    }

    @Test
    void eventToNotification() throws Exception {
        createSubscriptionThatPublishesImmediately();
        sendEvent();
        await().untilAsserted(() -> {
            assertThat(notificationsListener.getReceivedNotifications()).hasSize(1);
            assertThat(notificationsListener.getReceivedNotifications().getFirst().getTitle()).isEqualTo("Ironing");
        });
    }

    private void createSubscriptionThatPublishesImmediately() throws Exception {
        String mappingOfTitle = "data['taskName']";
        String mappingOfMessage = "'Task ' + data['taskName'] + ' is expiring. Due date was ' + data['dueDate'] + '.'";
        String mappingOfActionText = "'Open Housagotchi'";
        String mappingOfActionUrl = "data['url']";
        SubscriptionMappingToNotificationEmbeddable mapping = new SubscriptionMappingToNotificationEmbeddable(mappingOfTitle, mappingOfMessage, mappingOfActionText, mappingOfActionUrl, null);
        SubscriptionEntity subscription = new SubscriptionEntity(1L, "Housagotchi", "true", "false", mapping, PUBLISH_IMMEDIATELY);
        var subscriptionAsJson = objectMapper.writeValueAsString(subscription);

        mockMvc.perform(post("/api/subscription/")
                .header("Authorization", "Bearer " + getAccessToken())
                .contentType("application/json")
                .content(subscriptionAsJson))
            .andExpect(status().isOk());
    }

    private void sendEvent() throws JsonProcessingException {
        HashMap<String, String> data = new HashMap<>();
        data.put("taskName", "Ironing");
        data.put("dueDate", "08/04/2018");
        data.put("url", "http://housagotchi");

        LocalDateTime publishDate = LocalDateTime.now();
        Event event = new Event("Housagotchi", "1", FlowAction.START, publishDate, data);

        String eventsJson = objectMapper.writeValueAsString(List.of(event));

        var message = MessageBuilder.withBody(eventsJson.getBytes())
            .setContentType(MessageProperties.CONTENT_TYPE_JSON)
            .build();
        rabbitTemplate.send("eventTopic", "eventTopic", message);
    }

    @SneakyThrows
    private String getAccessToken() {
        URI authorizationURI = new URIBuilder(keycloakContainer.getAuthServerUrl() + "/realms/portal-realm/protocol/openid-connect/token").build();
        RestClient webclient = RestClient.builder().build();
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.put("grant_type", Collections.singletonList("password"));
        formData.put("client_id", Collections.singletonList("portal-client"));
        formData.put("username", Collections.singletonList("jane.doe@baeldung.com"));
        formData.put("password", Collections.singletonList("s3cr3t"));

        return Optional.ofNullable(
                webclient.post()
                    .uri(authorizationURI)
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .body(formData)
                    .retrieve()
                    .body(KeyCloakToken.class)
            ).map(KeyCloakToken::accessToken)
            .orElseThrow();
    }

    record KeyCloakToken(@JsonProperty("access_token") String accessToken) {
    }
}
