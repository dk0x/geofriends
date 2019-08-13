package net.thumbtack.geofriends;


import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Server {

//    private final static Logger LOGGER = LoggerFactory.getLogger(Server.class);

    public static void main(String[] args) {
//        LOGGER.debug("Enter in Server.main(args = {})", args);
        final SpringApplication springApplication = new SpringApplication(Server.class);
        //springApplication.setBannerMode(Banner.Mode.OFF);
        springApplication.run(args);
    }
}
