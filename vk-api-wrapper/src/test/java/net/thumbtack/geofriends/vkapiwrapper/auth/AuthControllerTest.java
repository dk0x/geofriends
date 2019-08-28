package net.thumbtack.geofriends.vkapiwrapper.auth;

import net.thumbtack.geofriends.vkapiwrapper.TestHelper;
import net.thumbtack.geofriends.vkapiwrapper.shared.Session;
import net.thumbtack.geofriends.vkapiwrapper.shared.VkApiConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.cookie;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = AuthController.class)
public class AuthControllerTest {
    @Autowired
    private MockMvc mvc;
    @MockBean
    private AuthService authService;

    @Test
    public void auth_whenCorrect_mustHttpStatusOk() throws Exception {
        when(authService.authByCode(anyString(), anyString())).
                thenReturn(new Session(TestHelper.TEST_SESSION_ID, TestHelper.TEST_ACCESS_TOKEN));

        ResultActions resultActions = mvc.perform(
                post("/api/vk/auth").
                        param("code", TestHelper.TEST_CORRECT_CODE));

        resultActions.andExpect(status().isOk());
    }

    @Test
    public void auth_whenCorrect_mustSetCorrectCookie() throws Exception {
        when(authService.authByCode(anyString(), anyString())).
                thenReturn(new Session(TestHelper.TEST_SESSION_ID, TestHelper.TEST_ACCESS_TOKEN));

        ResultActions resultActions = mvc.perform(
                post("/api/vk/auth").
                        param("code", TestHelper.TEST_CORRECT_CODE));

        resultActions.andExpect(cookie().value(VkApiConfig.SESSION_COOKIE_NAME, TestHelper.TEST_SESSION_ID));
    }
}