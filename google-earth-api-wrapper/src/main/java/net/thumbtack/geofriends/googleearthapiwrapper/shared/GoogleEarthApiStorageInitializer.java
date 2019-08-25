package net.thumbtack.geofriends.googleearthapiwrapper.shared;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.thumbtack.geofriends.dynamodbwrapper.TableManager;
import net.thumbtack.geofriends.googleearthapiwrapper.city.City;
import net.thumbtack.geofriends.googleearthapiwrapper.city.CityRepository;
import org.socialsignin.spring.data.dynamodb.repository.config.EnableDynamoDBRepositories;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@AllArgsConstructor
@EnableDynamoDBRepositories(basePackageClasses = {CityRepository.class})
@Slf4j
public class GoogleEarthApiStorageInitializer {
    private TableManager tableManager;

    @PostConstruct
    public void init() {
        log.debug("Enter in DatabaseInitialization.init()");

        tableManager.createTable(City.class);

        log.debug("Exit from DatabaseInitialization.init()");
    }
}
