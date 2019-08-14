package net.thumbtack.geofriends.vkapiwrapper.shared;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.model.ResourceInUseException;
import lombok.AllArgsConstructor;
import net.thumbtack.geofriends.vkapiwrapper.auth.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@AllArgsConstructor
public class DatabaseInitialization {
    private final static Logger log = LoggerFactory.getLogger(DatabaseInitialization.class);

    private DynamoDBMapper dynamoDBMapper;
    private DynamoDB dynamoDB;

    @PostConstruct
    public void init() {
        log.debug("Enter in DatabaseInitialization.init()");
        createTable(Session.class);
        log.debug("Exit from DatabaseInitialization.init()");
    }

    private <T> void createTable(Class<T> tClass) {
        CreateTableRequest createTableSessionRequest = dynamoDBMapper.generateCreateTableRequest(tClass);
        createTableSessionRequest.setProvisionedThroughput(new ProvisionedThroughput(1L, 1L));
        try {
            log.info("Attempt to create '{}' table.", tClass.getSimpleName());
            dynamoDB.createTable(createTableSessionRequest);
            log.info("Table '{}' created successful.", tClass.getSimpleName());
        } catch (ResourceInUseException ex) {
            log.info("'{}' table already exist.", tClass.getSimpleName());
        }
        log.debug("Exit from DatabaseInitialization.init()");

    }

}
