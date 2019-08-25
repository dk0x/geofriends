package net.thumbtack.geofriends.vkapiwrapper.forgotme;

import net.thumbtack.geofriends.vkapiwrapper.TestHelper;
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

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.cookie;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@WebMvcTest(controllers = ForgetMeController.class)
public class ForgetMeControllerTest {
    @Autowired
    private MockMvc mvc;
    @MockBean
    private ForgetMeService forgetMeService;

    @Test
    public void forgetMe_mustHttpStatusOk() throws Exception {
        ResultActions resultActions = mvc.perform(
                delete("/api/vk/forgetMe").
                        cookie(new Cookie(VkApiConfig.SESSION_COOKIE_NAME, TestHelper.TEST_SESSION_ID)));

        resultActions.andExpect(status().isOk());
    }

    @Test
    public void forgetMe_withoutCookieInRequest_mustHttpStatusOk() throws Exception {
        ResultActions resultActions = mvc.perform(
                delete("/api/vk/forgetMe"));

        resultActions.andExpect(status().isOk());
    }

    @Test
    public void forgetMe_mustSetBlankCookie() throws Exception {
        ResultActions resultActions = mvc.perform(
                delete("/api/vk/forgetMe").
                        cookie(new Cookie(VkApiConfig.SESSION_COOKIE_NAME, TestHelper.TEST_SESSION_ID)));

        resultActions.andExpect(cookie().value(VkApiConfig.SESSION_COOKIE_NAME, ""));
    }

    @Test
    public void forgetMe_mustCallServiceWithCorrectSessionId() throws Exception {
        mvc.perform(
                delete("/api/vk/forgetMe").
                        cookie(new Cookie(VkApiConfig.SESSION_COOKIE_NAME, TestHelper.TEST_SESSION_ID)));

        verify(forgetMeService).forgetMe(eq(TestHelper.TEST_SESSION_ID));
    }
}