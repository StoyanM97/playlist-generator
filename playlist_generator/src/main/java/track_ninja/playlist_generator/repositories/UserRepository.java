package track_ninja.playlist_generator.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import track_ninja.playlist_generator.models.User;

import java.util.List;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {
    List<User> findAllByEnabledTrue();

    boolean existsByUsername(String name);

    User findByUsernameAndEnabledTrue(String username);

    List<User> findAllByEnabledTrueAndUsernameLike(String username);
}
