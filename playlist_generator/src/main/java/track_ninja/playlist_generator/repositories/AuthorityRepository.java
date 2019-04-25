package track_ninja.playlist_generator.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import track_ninja.playlist_generator.models.Authority;
import track_ninja.playlist_generator.models.commons.UserRole;

@Repository
public interface AuthorityRepository extends CrudRepository<Authority, Integer> {
    Authority findByName(UserRole userRole);
}
