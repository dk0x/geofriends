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
    private VkApiConfig vkApiConfig;
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
        when(vkOAuthProvider.exchangeCodeForAccessToken(any(), anyInt(), any(), any())).
                thenReturn(TestHelper.TEST_ACCESS_TOKEN);
        AuthService authService = new AuthService(vkApiConfig, vkOAuthProvider, sessionRepository);

        Session session = authService.authByCode(TestHelper.TEST_CORRECT_CODE);

        verify(sessionRepository).save(eq(session));
    }

    @Test
    public void authByCode_whenCorrect_mustReturnValidSession() throws ClientException, ApiException {
        when(vkOAuthProvider.exchangeCodeForAccessToken(eq(TestHelper.TEST_CORRECT_CODE), anyInt(), any(), any())).
                thenReturn(TestHelper.TEST_ACCESS_TOKEN);
        AuthService authService = new AuthService(vkApiConfig, vkOAuthProvider, sessionRepository);

        Session session = authService.authByCode(TestHelper.TEST_CORRECT_CODE);

        assertThat(session.getSessionId()).isNotBlank();
        assertThat(session.getAccessToken()).isEqualTo(TestHelper.TEST_ACCESS_TOKEN);
    }

}