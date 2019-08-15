package net.thumbtack.geofriends.dynamodbwrapper;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.model.ResourceInUseException;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class TableManager {
    private final static Logger log = LoggerFactory.getLogger(TableManager.class);

    private DynamoDBMapper dynamoDBMapper;
    private DynamoDB dynamoDB;

    public <T> void createTable(Class<T> tClass) {
        CreateTableRequest createTableSessionRequest = dynamoDBMapper.generateCreateTableRequest(tClass);
        createTableSessionRequest.setProvisionedThroughput(new ProvisionedThroughput(1L, 1L));
        try {
            log.info("Attempt to create '{}' table.", tClass.getSimpleName());
            dynamoDB.createTable(createTableSessionRequest);
            log.info("Table '{}' created successful.", tClass.getSimpleName());
        } catch (ResourceInUseException ex) {
            log.info("'{}' table already exist.", tClass.getSimpleName());
        }
    }

}
