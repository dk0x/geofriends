package net.thumbtack.geofriends.vkapiwrapper.shared;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
@DynamoDBTable(tableName = "Session")
public class Session {
    @DynamoDBHashKey(attributeName = "sessionId")
    private String sessionId;
    private String accessToken;
}
