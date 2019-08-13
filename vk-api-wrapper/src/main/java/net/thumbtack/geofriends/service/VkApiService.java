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
import lombok.AllArgsConstructor;
import net.thumbtack.geofriends.config.VkApiConfig;
import net.thumbtack.geofriends.dto.response.PersonInfoDtoResponse;
import net.thumbtack.geofriends.exceptions.AuthenticationBrokenException;
import net.thumbtack.geofriends.exceptions.OauthCodeInvalidException;
import net.thumbtack.geofriends.exceptions.SessionNotFoundException;
import net.thumbtack.geofriends.model.AuthSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class VkApiService {
    private final static Logger log = LoggerFactory.getLogger(VkApiService.class);
    private final static Map<String, AuthSession> SESSION_STORAGE_TEMP = new HashMap<>();

    private VkApiConfig vkApiConfig;

    public AuthSession authByCode(String code) throws OauthCodeInvalidException {
        log.debug("Enter in VkApiService.authByCode(code = {})", code);

        String accessToken = exchangeCodeForAccessToken(code);
        AuthSession authSession = createAndSaveSession(generateNewSessionId(), accessToken);

        log.debug("Exit from VkApiService.authByCode() with return {}", authSession);
        return authSession;
    }

    private String exchangeCodeForAccessToken(String code) throws OauthCodeInvalidException {
        log.debug("Enter in VkApiService.exchangeCodeForAccessToken(code = {})", code);

        VkApiClient vkApiClient = new VkApiClient(HttpTransportClient.getInstance());
        OAuthUserAuthorizationCodeFlowQuery query = vkApiClient.oAuth()
                .userAuthorizationCodeFlow(vkApiConfig.getAppId(), vkApiConfig.getClientSecret(),
                        vkApiConfig.getAuthorizeRedirectUri(), code);

        UserAuthResponse authResponse;
        try {
            authResponse = query.execute();
        } catch (ApiException | ClientException e) {
            OauthCodeInvalidException ex = new OauthCodeInvalidException(e);
            log.debug("Exit from VkApiService.exchangeCodeForAccessToken() by throwing {} with message {}",
                    ex.getClass().getName(), ex.getMessage());
            throw ex;
        }

        String accessToken = authResponse.getAccessToken();
        log.debug("Exit from VkApiService.exchangeCodeForAccessToken() with return {}", accessToken);
        return accessToken;
    }

    private AuthSession createAndSaveSession(String sessionId, String accessToken) {
        AuthSession authSession = new AuthSession(sessionId, accessToken);
        SESSION_STORAGE_TEMP.put(authSession.getSessionId(), authSession);
        return authSession;
    }

    private String generateNewSessionId() {
        return UUID.randomUUID().toString();
    }


    public List<PersonInfoDtoResponse> getFriends(String sessionId) throws SessionNotFoundException, AuthenticationBrokenException {
        log.debug("Enter in VkApiService.getFriends(sessionId = {})", sessionId);

        AuthSession authSession = getSessionById(sessionId);
        List<UserXtrLists> friends = fetchFriends(authSession.getAccessToken());
        List<PersonInfoDtoResponse> persons = convertFriendsToPersons(friends);

        log.debug("Exit from VkApiService.getFriends() with return {}", persons);
        return persons;
    }

    private AuthSession getSessionById(String sessionId) throws SessionNotFoundException {
        AuthSession authSession = SESSION_STORAGE_TEMP.getOrDefault(sessionId, null);
        if (authSession == null) {
            SessionNotFoundException ex = new SessionNotFoundException();
            log.debug("Exit from VkApiService.getSessionById() by throwing {} with message {}", ex.getClass().getName(),
                    ex.getMessage());
            throw ex;
        }
        return authSession;
    }

    private List<UserXtrLists> fetchFriends(String accessToken) throws AuthenticationBrokenException {
        HttpTransportClient httpClient = HttpTransportClient.getInstance();
        VkApiClient vkApiClient = new VkApiClient(httpClient);

        UserActor actor = new UserActor(vkApiConfig.getAppId(), accessToken);
        FriendsGetQueryWithFields query = vkApiClient.friends().getWithFields(actor, Fields.CITY, Fields.COUNTRY, Fields.PHOTO_50);
        GetFieldsResponse response;
        try {
            response = query.execute();
        } catch (ApiException | ClientException e) {
            AuthenticationBrokenException ex = new AuthenticationBrokenException(e);
            log.debug("Exit from VkApiService.fetchFriends() by throwing {} with message {}", ex.getClass().getName(),
                    ex.getMessage());
            throw ex;
        }

        return response.getItems();
    }

    private List<PersonInfoDtoResponse> convertFriendsToPersons(List<UserXtrLists> input) {
        return input.stream().map(PersonInfoDtoResponse::createFromUserXtrLists).collect(Collectors.toList());
    }
}
