package net.thumbtack.geofriends.vkapiwrapper.shared;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.model.ResourceInUseException;
import lombok.Getter;
import lombok.Setter;
import org.socialsignin.spring.data.dynamodb.repository.config.EnableDynamoDBRepositories;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "dynamodb")
@Setter
@Getter
@EnableDynamoDBRepositories(basePackages = "net.thumbtack.geofriends.vkapiwrapper.shared")
public class DynamoDBConfig {
    private String endpoint;
    private String region;
    private String accessKey;
    private String secretKey;

    public void createTables() {
    }

    @Bean
    public DynamoDB dynamoDB() {
        AmazonDynamoDB amazonDynamoDB = amazonDynamoDB();
        DynamoDB dynamoDB = new DynamoDB(amazonDynamoDB);

        DynamoDBMapper dynamoDBMapper = new DynamoDBMapper(amazonDynamoDB);

        CreateTableRequest createTableRequest = dynamoDBMapper.generateCreateTableRequest(Session.class);
        createTableRequest.setProvisionedThroughput(new ProvisionedThroughput(1L, 1L));
        try {
            amazonDynamoDB.createTable(createTableRequest);
        } catch (ResourceInUseException e) {

        }

        return dynamoDB;
    }

    @Bean
    public AmazonDynamoDB amazonDynamoDB() {
        AmazonDynamoDB amazonDynamoDB = AmazonDynamoDBClientBuilder.standard()
                .withCredentials(awsCredentialsProvider())
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(endpoint, region))
                .build();

//        DynamoDBMapper dynamoDBMapper = new DynamoDBMapper(amazonDynamoDB);
//
//        CreateTableRequest createTableRequest = dynamoDBMapper.generateCreateTableRequest(Session.class);
//        createTableRequest.setProvisionedThroughput(new ProvisionedThroughput(1L, 1L));
//        amazonDynamoDB.createTable(createTableRequest);

        return amazonDynamoDB;
    }

    @Bean
    public AWSCredentialsProvider awsCredentialsProvider() {
        return new AWSStaticCredentialsProvider(awsCredentials());
    }

    @Bean
    public AWSCredentials awsCredentials() {
        return new BasicAWSCredentials(accessKey, secretKey);
    }

}
