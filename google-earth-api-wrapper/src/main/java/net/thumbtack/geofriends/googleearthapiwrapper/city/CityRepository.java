package net.thumbtack.geofriends.googleearthapiwrapper.city;

import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

@EnableScan
public interface CityRepository extends CrudRepository<City, Integer> {
}
