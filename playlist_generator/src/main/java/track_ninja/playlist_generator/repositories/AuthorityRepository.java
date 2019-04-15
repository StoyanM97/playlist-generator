package track_ninja.playlist_generator.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import track_ninja.playlist_generator.models.Authority;

@Repository
public interface AuthorityRepository extends CrudRepository<Authority, Long> {
}
