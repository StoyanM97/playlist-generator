package track_ninja.playlist_generator.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import track_ninja.playlist_generator.models.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    boolean existsByUsername(String name);

    User findByUsername(String username);
}
