package net.thumbtack.geofriends.vkapiwrapper.shared;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.thumbtack.geofriends.dynamodbwrapper.TableManager;
import net.thumbtack.geofriends.vkapiwrapper.auth.Session;
import net.thumbtack.geofriends.vkapiwrapper.auth.SessionRepository;
import org.socialsignin.spring.data.dynamodb.repository.config.EnableDynamoDBRepositories;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@AllArgsConstructor
@Slf4j
@EnableDynamoDBRepositories(basePackageClasses = {SessionRepository.class})
public class VkApiStorageInitializer {
    private TableManager tableManager;

    @PostConstruct
    public void init() {
        log.debug("Enter in DatabaseInitialization.init()");

        tableManager.createTable(Session.class);

        log.debug("Exit from DatabaseInitialization.init()");
    }

}
