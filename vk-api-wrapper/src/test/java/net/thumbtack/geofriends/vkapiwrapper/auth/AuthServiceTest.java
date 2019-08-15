package net.thumbtack.geofriends.vkapiwrapper.auth;

import com.vk.api.sdk.actions.OAuth;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.UserAuthResponse;
import com.vk.api.sdk.queries.oauth.OAuthUserAuthorizationCodeFlowQuery;
import net.thumbtack.geofriends.vkapiwrapper.shared.VkApiConfig;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AuthServiceTest {
    private static final String TEST_CORRECT_CODE = "correctCode";
    private static final String TEST_ACCESS_TOKEN = "accessToken";

    @Mock
    private VkApiConfig vkApiConfig;
    @Mock
    private SessionRepository sessionRepository;
    @Mock
    private VkApiClient vkApiClient;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void authByCode_whenCorrect_mustSaveAndReturnSession() throws ClientException, ApiException {
        OAuth oAuth = mock(OAuth.class);
        when(vkApiClient.oAuth()).thenReturn(oAuth);
        OAuthUserAuthorizationCodeFlowQuery query = mock(OAuthUserAuthorizationCodeFlowQuery.class);
        when(oAuth.userAuthorizationCodeFlow(any(), any(), any(), any())).thenReturn(query);
        UserAuthResponse response = mock(UserAuthResponse.class);
        when(query.execute()).thenReturn(response);
        when(response.getAccessToken()).thenReturn(TEST_ACCESS_TOKEN);

        AuthService authService = new AuthService(vkApiConfig, vkApiClient, sessionRepository);
        Session session = authService.authByCode(TEST_CORRECT_CODE);

        verify(sessionRepository).save(eq(session));
        assertThat(session.getSessionId()).isNotBlank();
        assertThat(session.getAccessToken()).isEqualTo(TEST_ACCESS_TOKEN);
    }

}