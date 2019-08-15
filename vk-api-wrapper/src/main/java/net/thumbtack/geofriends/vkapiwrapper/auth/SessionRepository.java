package net.thumbtack.geofriends.vkapiwrapper.auth;

import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

@EnableScan
public interface SessionRepository extends CrudRepository<Session, String> {
}
