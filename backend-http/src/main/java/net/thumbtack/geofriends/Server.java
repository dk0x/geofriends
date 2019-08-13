package net.thumbtack.geofriends;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Server {
    public static void main(String[] args) {
        final SpringApplication springApplication = new SpringApplication(Server.class);
        springApplication.run(args);
    }
}
