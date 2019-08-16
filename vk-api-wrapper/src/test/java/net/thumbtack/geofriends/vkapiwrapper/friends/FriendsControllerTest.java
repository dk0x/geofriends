package net.thumbtack.geofriends.vkapiwrapper.friends;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.thumbtack.geofriends.vkapiwrapper.shared.VkApiConfig;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import javax.servlet.http.Cookie;
import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = FriendsController.class)
public class FriendsControllerTest {

    @Autowired
    private MockMvc mvc;
    @MockBean
    private FriendsService friendsService;
    @Autowired
    private ObjectMapper mapper;

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void friends_whenCorrect_mustReturnPeople() throws Exception {
        ArrayList<PersonDtoResponse> personDtoResponses = new ArrayList<>();
        personDtoResponses.add(new PersonDtoResponse(1, "fn", "ln", 2, "city", 3, "country", "photoUri"));
        when(friendsService.getFriends(anyString())).thenReturn(personDtoResponses);

        mvc.perform(get("/api/vk/friends")
                .cookie(new Cookie(VkApiConfig.SESSION_COOKIE_NAME, "sesionId"))
        ).andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(personDtoResponses)));

    }
}