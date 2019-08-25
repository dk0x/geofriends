package net.thumbtack.geofriends.vkapiwrapper.shared;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vk.api.sdk.exceptions.ApiAuthException;
import net.thumbtack.geofriends.vkapiwrapper.TestHelper;
import net.thumbtack.geofriends.vkapiwrapper.auth.AuthController;
import net.thumbtack.geofriends.vkapiwrapper.auth.AuthService;
import net.thumbtack.geofriends.vkapiwrapper.friends.FriendsController;
import net.thumbtack.geofriends.vkapiwrapper.friends.FriendsService;
import net.thumbtack.geofriends.vkapiwrapper.shared.VkApiExceptionHandlingController.ErrorDtoResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.servlet.http.Cookie;

import static net.thumbtack.geofriends.vkapiwrapper.shared.VkApiExceptionHandlingController.DESCRIPTION_API_AUTH_EXCEPTION;
import static net.thumbtack.geofriends.vkapiwrapper.shared.VkApiExceptionHandlingController.DESCRIPTION_SESSION_EXPIRED_EXCEPTION;
import static net.thumbtack.geofriends.vkapiwrapper.shared.VkApiExceptionHandlingController.ErrorCode.SESSION_EXPIRED;
import static net.thumbtack.geofriends.vkapiwrapper.shared.VkApiExceptionHandlingController.ErrorCode.VK_AUTH_EXPIRED;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
public class VkApiExceptionHandlingControllerTest {
    @MockBean
    private AuthService authService;
    @MockBean
    private FriendsService friendsService;
    private MockMvc mvc;
    private ObjectMapper mapper;

    @Before
    public void setUp() {
        mvc = MockMvcBuilders.
                standaloneSetup(new AuthController(authService), new FriendsController(friendsService)).
                setControllerAdvice(VkApiExceptionHandlingController.class).
                build();
        mapper = new ObjectMapper();
    }

    @Test
    public void auth_whenApiAuthException_mustStatusForbidden() throws Exception {
        when(authService.authByCode(anyString())).thenThrow(new ApiAuthException(""));

        ResultActions resultActions = mvc.perform(
                post("/api/vk/auth").
                        param("code", TestHelper.TEST_CORRECT_CODE));

        resultActions.andExpect(status().isForbidden());
    }

    @Test
    public void auth_whenApiAuthException_mustCorrectContentWithErrorCodeAndDescription() throws Exception {
        when(authService.authByCode(anyString())).thenThrow(new ApiAuthException(""));
        ErrorDtoResponse errorDtoResponse = new ErrorDtoResponse(VK_AUTH_EXPIRED, DESCRIPTION_API_AUTH_EXCEPTION);

        ResultActions resultActions = mvc.perform(
                post("/api/vk/auth").
                        param("code", TestHelper.TEST_CORRECT_CODE));

        resultActions.andExpect(content().json(mapper.writeValueAsString(errorDtoResponse)));
    }

    @Test
    public void friends_whenApiAuthException_mustStatusForbidden() throws Exception {
        when(friendsService.getFriends(anyString())).thenThrow(new ApiAuthException(""));

        ResultActions resultActions = mvc.perform(
                get("/api/vk/friends").
                        cookie(new Cookie(VkApiConfig.SESSION_COOKIE_NAME, TestHelper.TEST_SESSION_ID)));

        resultActions.andExpect(status().isForbidden());
    }

    @Test
    public void friends_whenApiAuthException_mustCorrectContentWithErrorCodeAndDescription() throws Exception {
        when(friendsService.getFriends(anyString())).thenThrow(new ApiAuthException(""));
        ErrorDtoResponse errorDtoResponse = new ErrorDtoResponse(VK_AUTH_EXPIRED, DESCRIPTION_API_AUTH_EXCEPTION);

        ResultActions resultActions = mvc.perform(
                get("/api/vk/friends").
                        cookie(new Cookie(VkApiConfig.SESSION_COOKIE_NAME, TestHelper.TEST_SESSION_ID)));

        resultActions.andExpect(content().json(mapper.writeValueAsString(errorDtoResponse)));
    }


    @Test
    public void friends_whenSessionExpiredException_mustStatusForbidden() throws Exception {
        when(friendsService.getFriends(anyString())).thenThrow(new SessionExpiredException());

        ResultActions resultActions = mvc.perform(
                get("/api/vk/friends").
                        cookie(new Cookie(VkApiConfig.SESSION_COOKIE_NAME, TestHelper.TEST_SESSION_ID)));

        resultActions.andExpect(status().isForbidden());
    }

    @Test
    public void friends_whenSessionExpiredException_mustCorrectContentWithErrorCodeAndDescription() throws Exception {
        when(friendsService.getFriends(anyString())).thenThrow(new SessionExpiredException());
        ErrorDtoResponse errorDtoResponse = new ErrorDtoResponse(SESSION_EXPIRED, DESCRIPTION_SESSION_EXPIRED_EXCEPTION);

        ResultActions resultActions = mvc.perform(
                get("/api/vk/friends").
                        cookie(new Cookie(VkApiConfig.SESSION_COOKIE_NAME, TestHelper.TEST_SESSION_ID)));

        resultActions.andExpect(content().json(mapper.writeValueAsString(errorDtoResponse)));
    }
}