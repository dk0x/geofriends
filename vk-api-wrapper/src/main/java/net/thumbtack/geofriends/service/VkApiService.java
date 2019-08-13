package net.thumbtack.geofriends.service;

import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import com.vk.api.sdk.objects.UserAuthResponse;
import com.vk.api.sdk.objects.friends.UserXtrLists;
import com.vk.api.sdk.objects.friends.responses.GetFieldsResponse;
import com.vk.api.sdk.objects.users.Fields;
import com.vk.api.sdk.queries.friends.FriendsGetQueryWithFields;
import com.vk.api.sdk.queries.oauth.OAuthUserAuthorizationCodeFlowQuery;
import net.thumbtack.geofriends.config.VkApiConfig;
import net.thumbtack.geofriends.dto.response.PersonInfoDtoResponse;
import net.thumbtack.geofriends.exceptions.AuthenticationBrokenException;
import net.thumbtack.geofriends.exceptions.OauthCodeInvalidException;
import net.thumbtack.geofriends.exceptions.SessionNotFoundException;
import net.thumbtack.geofriends.model.AuthSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class VkApiService {
    private final static Logger LOGGER = LoggerFactory.getLogger(VkApiService.class);

    private final static Map<String, AuthSession> SESSION_STORAGE_TEMP = new HashMap<>();

    private VkApiConfig vkApiConfig;

    @Autowired
    public VkApiService(VkApiConfig vkApiConfig) {
        this.vkApiConfig = vkApiConfig;
    }


    public AuthSession authByCode(final String code) throws OauthCodeInvalidException {
        LOGGER.debug("Enter in VkApiService.authByCode(code = {})", code);

        final String accessToken = exchangeCodeForAccessToken(code);
        final AuthSession authSession = createAndSaveSession(generateNewSessionId(), accessToken);

        LOGGER.debug("Exit from VkApiService.authByCode() with return {}", authSession);
        return authSession;
    }

    private String exchangeCodeForAccessToken(final String code) throws OauthCodeInvalidException {
        LOGGER.debug("Enter in VkApiService.exchangeCodeForAccessToken(code = {})", code);

        final HttpTransportClient httpTransportClient = HttpTransportClient.getInstance();
        final VkApiClient vkApiClient = new VkApiClient(httpTransportClient);
        final OAuthUserAuthorizationCodeFlowQuery query = vkApiClient.oAuth()
                .userAuthorizationCodeFlow(vkApiConfig.getAppId(), vkApiConfig.getClientSecret(),
                        vkApiConfig.getAuthorizeRedirectUri(), code);

        final UserAuthResponse authResponse;
        try {
            authResponse = query.execute();
        } catch (ApiException | ClientException e) {
            final OauthCodeInvalidException ex = new OauthCodeInvalidException(e);
            LOGGER.debug("Exit from VkApiService.exchangeCodeForAccessToken() by throwing {} with message {}",
                    ex.getClass().getName(), ex.getMessage());
            throw ex;
        }

        final String accessToken = authResponse.getAccessToken();
        LOGGER.debug("Exit from VkApiService.exchangeCodeForAccessToken() with return {}", accessToken);
        return accessToken;
    }

    private AuthSession createAndSaveSession(final String sessionId, final String accessToken) {
        final AuthSession authSession = new AuthSession(sessionId, accessToken);
        SESSION_STORAGE_TEMP.put(authSession.getSessionId(), authSession);
        return authSession;
    }

    private String generateNewSessionId() {
        return UUID.randomUUID().toString();
    }


    public List<PersonInfoDtoResponse> getFriends(final String sessionId)
            throws SessionNotFoundException, AuthenticationBrokenException {
        LOGGER.debug("Enter in VkApiService.getFriends(sessionId = {})", sessionId);

        final AuthSession authSession = getSessionById(sessionId);
        final List<UserXtrLists> friends = fetchFriends(authSession.getAccessToken());
        final List<PersonInfoDtoResponse> persons = convertFriendsToPersons(friends);

        LOGGER.debug("Exit from VkApiService.getFriends() with return {}", persons);
        return persons;
    }

    private AuthSession getSessionById(final String sessionId) throws SessionNotFoundException {
        final AuthSession authSession = SESSION_STORAGE_TEMP.getOrDefault(sessionId, null);
        if (authSession == null) {
            final SessionNotFoundException ex = new SessionNotFoundException();
            LOGGER.debug("Exit from VkApiService.getSessionById() by throwing {} with message {}",
                    ex.getClass().getName(), ex.getMessage());
            throw ex;
        }
        return authSession;
    }

    private List<UserXtrLists> fetchFriends(final String accessToken) throws AuthenticationBrokenException {
        final HttpTransportClient httpClient = HttpTransportClient.getInstance();
        final VkApiClient vkApiClient = new VkApiClient(httpClient);

        final UserActor actor = new UserActor(vkApiConfig.getAppId(), accessToken);
        FriendsGetQueryWithFields query = vkApiClient.friends().getWithFields(actor, Fields.CITY, Fields.COUNTRY, Fields.PHOTO_50);
        final GetFieldsResponse response;
        try {
            response = query.execute();
        } catch (ApiException | ClientException e) {
            final AuthenticationBrokenException ex = new AuthenticationBrokenException(e);
            LOGGER.debug("Exit from VkApiService.fetchFriends() by throwing {} with message {}",
                    ex.getClass().getName(), ex.getMessage());
            throw ex;
        }

        return response.getItems();
    }

    private List<PersonInfoDtoResponse> convertFriendsToPersons(List<UserXtrLists> input) {
        return input.stream()
                .map(PersonInfoDtoResponse::createFromUserXtrLists)
                .collect(Collectors.toList());
    }
}
