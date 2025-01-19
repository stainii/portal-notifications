package be.stijnhooft.portal.notifications.controllers;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.springtestdbunit.assertion.DatabaseAssertionMode;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.test.web.servlet.MockMvc;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("test")
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
    DirtiesContextTestExecutionListener.class,
    TransactionalTestExecutionListener.class,
    DbUnitTestExecutionListener.class})
@AutoConfigureMockMvc
public class NotificationControllerTest {

    @Autowired
    private MockMvc mvc;

    @Test
    @DatabaseSetup("/datasets/NotificationControllerTest-find-initial.xml")
    @DatabaseTearDown("/datasets/clear.xml")
    public void findWhenRequestingOnlyUnreadAndPublishedNotifications() throws Exception {
        mvc.perform(get("/api/notification/")
                .param("onlyUnread", "true")
                .contentType(APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(2)))

            .andExpect(jsonPath("$[0].id", is(3)))
            .andExpect(jsonPath("$[0].origin", is("Housagotchi")))
            .andExpect(jsonPath("$[0].date", is("2017-10-24T10:02:10")))
            .andExpect(jsonPath("$[0].title", is("Title 3")))
            .andExpect(jsonPath("$[0].message", is("Message 3")))
            .andExpect(jsonPath("$[0].action.url", is("https://please_overwrite/api/notification/3/action/url/")))
            .andExpect(jsonPath("$[0].action.text", is("Open Housagotchi")))
            .andExpect(jsonPath("$[0].publishStrategy", is("PUBLISH_IMMEDIATELY")))

            .andExpect(jsonPath("$[1].id", is(1)))
            .andExpect(jsonPath("$[1].origin", is("Housagotchi")))
            .andExpect(jsonPath("$[1].date", is("2017-10-20T10:02:10")))
            .andExpect(jsonPath("$[1].title", is("Title 1")))
            .andExpect(jsonPath("$[1].message", is("Message 1")))
            .andExpect(jsonPath("$[1].action.url", is("https://please_overwrite/api/notification/1/action/url/")))
            .andExpect(jsonPath("$[1].action.text", is("Open Housagotchi")))
            .andExpect(jsonPath("$[1].publishStrategy", is("PUBLISH_IMMEDIATELY")));
    }

    @Test
    @DatabaseSetup("/datasets/NotificationControllerTest-find-initial.xml")
    @DatabaseTearDown("/datasets/clear.xml")
    public void findWhenRequestingOnlyReadNotifications() throws Exception {
        mvc.perform(get("/api/notification/")
                .param("onlyUnread", "false")
                .contentType(APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(1)))

            .andExpect(jsonPath("$[0].id", is(2)))
            .andExpect(jsonPath("$[0].origin", is("Housagotchi")))
            .andExpect(jsonPath("$[0].date", is("2017-10-22T10:02:10")))
            .andExpect(jsonPath("$[0].title", is("Title 2")))
            .andExpect(jsonPath("$[0].message", is("Message 2")))
            .andExpect(jsonPath("$[0].action.url", is("https://please_overwrite/api/notification/2/action/url/")))
            .andExpect(jsonPath("$[0].action.text", is("Open Housagotchi")))
            .andExpect(jsonPath("$[0].publishStrategy", is("PUBLISH_IMMEDIATELY")));
    }

    @Test
    @DatabaseSetup("/datasets/NotificationControllerTest-find-initial.xml")
    @DatabaseTearDown("/datasets/clear.xml")
    public void findWhenWeDoNotDefineOnlyRead() throws Exception {
        mvc.perform(get("/api/notification/")
                .contentType(APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(3)))

            .andExpect(jsonPath("$[0].id", is(3)))
            .andExpect(jsonPath("$[0].origin", is("Housagotchi")))
            .andExpect(jsonPath("$[0].date", is("2017-10-24T10:02:10")))
            .andExpect(jsonPath("$[0].title", is("Title 3")))
            .andExpect(jsonPath("$[0].message", is("Message 3")))
            .andExpect(jsonPath("$[0].action.url", is("https://please_overwrite/api/notification/3/action/url/")))
            .andExpect(jsonPath("$[0].action.text", is("Open Housagotchi")))
            .andExpect(jsonPath("$[0].publishStrategy", is("PUBLISH_IMMEDIATELY")))

            .andExpect(jsonPath("$[1].id", is(2)))
            .andExpect(jsonPath("$[1].origin", is("Housagotchi")))
            .andExpect(jsonPath("$[1].date", is("2017-10-22T10:02:10")))
            .andExpect(jsonPath("$[1].title", is("Title 2")))
            .andExpect(jsonPath("$[1].message", is("Message 2")))
            .andExpect(jsonPath("$[1].action.url", is("https://please_overwrite/api/notification/2/action/url/")))
            .andExpect(jsonPath("$[1].action.text", is("Open Housagotchi")))
            .andExpect(jsonPath("$[1].publishStrategy", is("PUBLISH_IMMEDIATELY")))

            .andExpect(jsonPath("$[2].id", is(1)))
            .andExpect(jsonPath("$[2].origin", is("Housagotchi")))
            .andExpect(jsonPath("$[2].date", is("2017-10-20T10:02:10")))
            .andExpect(jsonPath("$[2].title", is("Title 1")))
            .andExpect(jsonPath("$[2].message", is("Message 1")))
            .andExpect(jsonPath("$[2].action.url", is("https://please_overwrite/api/notification/1/action/url/")))
            .andExpect(jsonPath("$[2].action.text", is("Open Housagotchi")))
            .andExpect(jsonPath("$[2].publishStrategy", is("PUBLISH_IMMEDIATELY")));
    }

    @Test
    @DatabaseSetup("/datasets/NotificationControllerTest-markAsReadWhenReadIsTrue-initial.xml")
    @ExpectedDatabase(value = "/datasets/NotificationControllerTest-markAsReadWhenReadIsTrue-expected.xml", assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED)
    @DatabaseTearDown("/datasets/clear.xml")
    public void markAsReadWhenReadIsTrueSuccess() throws Exception {
        mvc.perform(put("/api/notification/1/read/")
                .content("""
                    {
                        "id":"1",
                        "read":"true"
                    }
                    """)
                .contentType(APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("id", is(1)))
            .andExpect(jsonPath("origin", is("Housagotchi")))
            .andExpect(jsonPath("date", is("2017-10-20T10:02:10")))
            .andExpect(jsonPath("title", is("Title 1")))
            .andExpect(jsonPath("message", is("Message 1")))
            .andExpect(jsonPath("action.url", is("https://please_overwrite/api/notification/1/action/url/")))
            .andExpect(jsonPath("action.text", is("Open Housagotchi")))
            .andExpect(jsonPath("publishStrategy", is("PUBLISH_IMMEDIATELY")));
    }

    @Test
    @DatabaseSetup("/datasets/NotificationControllerTest-markAsReadWhenReadIsFalse-initial.xml")
    @ExpectedDatabase(value = "/datasets/NotificationControllerTest-markAsReadWhenReadIsFalse-expected.xml", assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED)
    @DatabaseTearDown("/datasets/clear.xml")
    public void markAsReadWhenReadIsFalseSuccess() throws Exception {
        mvc.perform(put("/api/notification/1/read/")
                .content("{" +
                    "\"id\":\"1\"," +
                    "\"read\":\"false\"" +
                    "}")
                .contentType(APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("id", is(1)))
            .andExpect(jsonPath("origin", is("Housagotchi")))
            .andExpect(jsonPath("date", is("2017-10-20T10:02:10")))
            .andExpect(jsonPath("title", is("Title 1")))
            .andExpect(jsonPath("message", is("Message 1")))
            .andExpect(jsonPath("action.url", is("https://please_overwrite/api/notification/1/action/url/")))
            .andExpect(jsonPath("action.text", is("Open Housagotchi")))
            .andExpect(jsonPath("publishStrategy", is("PUBLISH_IMMEDIATELY")));
    }

    @Test
    @DatabaseSetup("/datasets/NotificationControllerTest-markAsReadWhenReadIsFalse-initial.xml")
    @DatabaseTearDown("/datasets/clear.xml")
    public void markAsReadWhenNotificationDoesNotExist() throws Exception {
        mvc.perform(put("/api/notification/2/read")
                .content("{" +
                    "\"id\":\"2\"," +
                    "\"read\":\"false\"" +
                    "}")
                .contentType(APPLICATION_JSON))
            .andExpect(status().isNotFound());
    }


    @Test
    @DatabaseSetup("/datasets/NotificationControllerTest-markAsReadWhenReadIsTrue-initial.xml")
    @ExpectedDatabase(value = "/datasets/NotificationControllerTest-markAsReadWhenReadIsTrue-expected.xml", assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED)
    @DatabaseTearDown("/datasets/clear.xml")
    public void markAsReadAndRedirectToActionUrlWhenSuccess() throws Exception {
        mvc.perform(get("/api/notification/1/action/url/"))
            .andExpect(redirectedUrl("http://www.stijnhooft.be"));
    }

    @Test
    @DatabaseSetup("/datasets/NotificationControllerTest-markAsReadWhenReadIsTrue-initial.xml")
    @DatabaseTearDown("/datasets/clear.xml")
    public void markAsReadAndRedirectToActionUrlWhenNotificationNotFound() throws Exception {
        mvc.perform(get("/api/notification/2/action/url/"))
            .andExpect(status().isNotFound());
    }

}
