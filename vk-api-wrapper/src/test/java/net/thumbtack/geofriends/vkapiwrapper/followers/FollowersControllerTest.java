package net.thumbtack.geofriends.vkapiwrapper.followers;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.thumbtack.geofriends.vkapiwrapper.TestHelper;
import net.thumbtack.geofriends.vkapiwrapper.shared.Person;
import net.thumbtack.geofriends.vkapiwrapper.shared.VkApiConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import javax.servlet.http.Cookie;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = FollowersController.class)
public class FollowersControllerTest {
    @Autowired
    private MockMvc mvc;
    @MockBean
    private FollowersService service;
    @Autowired
    private ObjectMapper mapper;

    @Test
    public void followers_mustHttpStatusOk() throws Exception {
        when(service.getFollowers(anyString())).thenReturn(new ArrayList<>());

        ResultActions resultActions = mvc.perform(
                get("/api/vk/followers").
                        cookie(new Cookie(VkApiConfig.SESSION_COOKIE_NAME, TestHelper.TEST_SESSION_ID)));

        resultActions.andExpect(status().isOk());
    }

    @Test
    public void followers_withoutCookie_mustHttpStatusBadRequest() throws Exception {
        ResultActions resultActions = mvc.perform(
                get("/api/vk/followers"));

        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    public void followers_mustReturnCorrectPeopleFromService() throws Exception {
        List<Person> personDtoRespons = Collections.singletonList(createTestPersonDtoResponse());
        when(service.getFollowers(anyString())).thenReturn(personDtoRespons);

        ResultActions resultActions = mvc.perform(
                get("/api/vk/followers").
                        cookie(new Cookie(VkApiConfig.SESSION_COOKIE_NAME, TestHelper.TEST_SESSION_ID)));

        resultActions.andExpect(content().json(mapper.writeValueAsString(personDtoRespons)));
    }

    private Person createTestPersonDtoResponse() {
        return new Person(1, "fn", "ln", 2, "city", 3, "country", "photoUri");
    }
}