package net.thumbtack.geofriends.vkapiwrapper.auth;

import net.thumbtack.geofriends.vkapiwrapper.shared.VkApiConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.cookie;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = AuthController.class)
public class AuthControllerTest {
    private static final String TEST_CORRECT_CODE = "correctCode";
    private static final String TEST_ACCESS_TOKEN = "accessToken";
    private static final String TEST_SESSION_ID = "sessionId";

    @Autowired
    private MockMvc mvc;
    @MockBean
    private AuthService authService;

    @Test
    public void auth_whenCorrect_mustSetCookie() throws Exception {
        when(authService.authByCode(anyString())).thenReturn(new Session(TEST_SESSION_ID, TEST_ACCESS_TOKEN));

        mvc.perform(post("/api/vk/auth")
                .param("code", TEST_CORRECT_CODE)
        ).andExpect(status().isOk())
                .andExpect(cookie().value(VkApiConfig.SESSION_COOKIE_NAME, TEST_SESSION_ID));
    }
}