package net.thumbtack.geofriends.vkapiwrapper.auth;

import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import net.thumbtack.geofriends.vkapiwrapper.TestHelper;
import net.thumbtack.geofriends.vkapiwrapper.shared.VkApiConfig;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AuthServiceTest {
    @Mock
    private SessionRepository sessionRepository;
    @Mock
    private VkOAuthProvider vkOAuthProvider;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void authByCode_whenCorrect_mustSaveSessionToStorage() throws ClientException, ApiException {
        when(vkOAuthProvider.exchangeCodeForAccessToken(any())).
                thenReturn(TestHelper.TEST_ACCESS_TOKEN);
        AuthService authService = new AuthService(vkOAuthProvider, sessionRepository);

        Session session = authService.authByCode(TestHelper.TEST_CORRECT_CODE);

        verify(sessionRepository).save(eq(session));
    }

    @Test
    public void authByCode_whenCorrect_mustReturnValidSession() throws ClientException, ApiException {
        when(vkOAuthProvider.exchangeCodeForAccessToken(eq(TestHelper.TEST_CORRECT_CODE))).
                thenReturn(TestHelper.TEST_ACCESS_TOKEN);
        AuthService authService = new AuthService(vkOAuthProvider, sessionRepository);

        Session session = authService.authByCode(TestHelper.TEST_CORRECT_CODE);

        assertThat(session.getSessionId()).isNotBlank();
        assertThat(session.getAccessToken()).isEqualTo(TestHelper.TEST_ACCESS_TOKEN);
    }

    @Test
    public void authByCode_whenTwiceCorrect_mustReturnDifferentSessionId() throws ClientException, ApiException {
        when(vkOAuthProvider.exchangeCodeForAccessToken(eq(TestHelper.TEST_CORRECT_CODE))).
                thenReturn(TestHelper.TEST_ACCESS_TOKEN);
        AuthService authService = new AuthService(vkOAuthProvider, sessionRepository);

        Session session1 = authService.authByCode(TestHelper.TEST_CORRECT_CODE);
        Session session2 = authService.authByCode(TestHelper.TEST_CORRECT_CODE);

        assertThat(session1.getSessionId()).isNotEqualTo(session2.getSessionId());
    }

}