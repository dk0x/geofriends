package net.thumbtack.geofriends.vkapiwrapper.friends;

import com.fasterxml.jackson.databind.ObjectMapper;
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
@WebMvcTest(controllers = FriendsController.class)
public class FriendsControllerTest {

    private static final String TEST_SESSION_ID = "testSessionId";

    @Autowired
    private MockMvc mvc;
    @MockBean
    private FriendsService friendsService;
    @Autowired
    private ObjectMapper mapper;

    @Test
    public void friends_mustHttpStatusOk() throws Exception {
        when(friendsService.getFriends(anyString())).thenReturn(new ArrayList<>());

        ResultActions resultActions = mvc.perform(get("/api/vk/friends").
                cookie(new Cookie(VkApiConfig.SESSION_COOKIE_NAME, TEST_SESSION_ID)));

        resultActions.andExpect(status().isOk());
    }

    @Test
    public void friends_withoutCookie_mustHttpStatusBadRequest() throws Exception {
        ResultActions resultActions = mvc.perform(get("/api/vk/friends"));

        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    public void friends_mustReturnCorrectPeopleFromService() throws Exception {
        List<PersonDtoResponse> personDtoResponses = Collections.singletonList(createTestPersonDtoResponse());
        when(friendsService.getFriends(anyString())).thenReturn(personDtoResponses);

        ResultActions resultActions = mvc.perform(get("/api/vk/friends").
                cookie(new Cookie(VkApiConfig.SESSION_COOKIE_NAME, TEST_SESSION_ID)));

        resultActions.andExpect(content().json(mapper.writeValueAsString(personDtoResponses)));
    }

    private PersonDtoResponse createTestPersonDtoResponse() {
        return new PersonDtoResponse(1, "fn", "ln", 2, "city", 3, "country", "photoUri");
    }
}