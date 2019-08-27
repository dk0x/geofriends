master:  [![Build Status](https://travis-ci.org/dk0x/geofriends.svg?branch=master)](https://travis-ci.org/dk0x/geofriends)  
develop: [![Build Status](https://travis-ci.org/dk0x/geofriends.svg?branch=develop)](https://travis-ci.org/dk0x/geofriends)  

# GeoFriends

How to setup dev environment:  
Lombok IDE plugins:  
IntelliJ IDEA  
```
Go to File > Settings > Plugins
Click on Browse repositories...
Search for Lombok Plugin
Click on Install plugin
Restart IntelliJ IDEA
```
Other IDE: https://projectlombok.org/setup/overview  

Repo:  
```
git clone https://github.com/dk0x/geofriends
cd geofriends
mvn clean package
mvn install
cp ./config/application.yml.template ./config/application.yml
```
Fill in application.yml file with your parameters.  
  
  
If you need VK application, Google Cloud Api Project or AWS DynamoDB:  

For VK:  
go https://vk.com/apps?act=manage and create application  
```
Platform: select Website
Website address: your domain name or http://localhost
Base domain: your host name or localhost
```
Click 'Connect website' button  
Go to settings  
Copy 'App ID' and 'Secure key' to application.yml (vk-api-id and vk-api-client-secret)  
Add to 'Authorized redirect URI' your [protocol]://[host]:[port] (80 port also required) and http://localhost:[port] for development  
Click 'Save' button.  
That's all for Vk.


For Google Api key:  
```
Go to https://cloud.google.com/console/google/maps-apis/overview  
From the Project drop-down menu, select or create the project for which you want to add an API key.  
From the  Navigation menu, select APIs & Services > Credentials.  
On the Credentials page, click Create credentials > API key.
```
Copy the Api key to application.yml (google-earth-api-key)  


For DynamoDB:  
Install AWS CLI https://aws.amazon.com/cli/  
In terminal
```
$ aws configure
AWS Access Key ID [None]: [your AccessKeyId or ex. test if dynamoDB running localy]
AWS Secret Access Key [None]: [your SecretAccesKey or ex. if test dynamoDB running localy]
Default region name [None]: [your AWS region or ex. eu-central-1]
Default output format [None]: json
```
Download runnable dynamoDB from https://docs.aws.amazon.com/en_us/amazondynamodb/latest/developerguide/DynamoDBLocal.DownloadingAndRunning.html  
Run it  
```
java -Djava.library.path=./DynamoDBLocal_lib -jar DynamoDBLocal.jar -sharedDb
```

That's all.  
Just make sure for your app can access to config file (./config/application.yml)  
  
To run jar:  
```
mvn package
java -jar "./backend-http/target/backend-http-0.1-SNAPSHOT.jar"
``` 