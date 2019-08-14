package net.thumbtack.geofriends.vkapiwrapper.shared;

import lombok.AllArgsConstructor;
import net.thumbtack.geofriends.dynamodbwrapper.TableManager;
import net.thumbtack.geofriends.vkapiwrapper.auth.Session;
import net.thumbtack.geofriends.vkapiwrapper.auth.SessionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.socialsignin.spring.data.dynamodb.repository.config.EnableDynamoDBRepositories;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@AllArgsConstructor
@EnableDynamoDBRepositories(basePackageClasses = {SessionRepository.class})
public class VkApiStorageInitializer {
    private final static Logger log = LoggerFactory.getLogger(VkApiStorageInitializer.class);

    private TableManager tableManager;

    @PostConstruct
    public void init() {
        log.debug("Enter in DatabaseInitialization.init()");

        tableManager.createTable(Session.class);

        log.debug("Exit from DatabaseInitialization.init()");
    }

}
