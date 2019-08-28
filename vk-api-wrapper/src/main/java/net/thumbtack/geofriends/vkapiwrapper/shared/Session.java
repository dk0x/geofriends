package net.thumbtack.geofriends.vkapiwrapper.shared;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@DynamoDBTable(tableName = "Session")
public class Session {
    @DynamoDBHashKey(attributeName = "sessionId")
    private String sessionId;
    private String accessToken;
}
