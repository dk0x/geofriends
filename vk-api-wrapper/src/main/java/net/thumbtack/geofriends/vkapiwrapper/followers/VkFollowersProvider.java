package net.thumbtack.geofriends.vkapiwrapper.followers;


import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.users.Fields;
import com.vk.api.sdk.objects.users.UserFull;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.thumbtack.geofriends.vkapiwrapper.shared.VkApiConfig;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
@AllArgsConstructor
public class VkFollowersProvider {
    private VkApiClient client;
    private VkApiConfig config;

    public List<UserFull> fetchVkFollowers(String accessToken) throws ClientException, ApiException {
        log.debug("Enter in VkFollowersProvider.fetchVkFollowers(accessToken = {})", accessToken);

        List<UserFull> vkPersons = client
                .users()
                .getFollowersWithFields(new UserActor(config.getAppId(), accessToken), Fields.CITY, Fields.COUNTRY, Fields.PHOTO_50)
                .execute()
                .getItems();

        log.debug("Exit from VkFollowersProvider.fetchVkFollowers() with return list size {}", vkPersons.size());
        return vkPersons;
    }
}
