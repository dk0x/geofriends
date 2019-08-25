package net.thumbtack.geofriends.vkapiwrapper.friends;

import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.friends.UserXtrLists;
import com.vk.api.sdk.objects.users.Fields;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.thumbtack.geofriends.vkapiwrapper.shared.VkApiConfig;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
@AllArgsConstructor
public class VkFriendsProvider {

    private VkApiClient client;
    private VkApiConfig config;

    public List<UserXtrLists> fetchVkFriends(String accessToken) throws ClientException, ApiException {
        log.debug("Enter in VkFriendsProvider.fetchVkFriends(accessToken = {})", accessToken);

        List<UserXtrLists> vkFriends = client
                .friends()
                .getWithFields(new UserActor(config.getAppId(), accessToken), Fields.CITY, Fields.COUNTRY, Fields.PHOTO_50)
                .execute()
                .getItems();

        log.debug("Exit from VkFriendsProvider.fetchVkFriends() with return list size {}", vkFriends.size());
        return vkFriends;
    }
}
