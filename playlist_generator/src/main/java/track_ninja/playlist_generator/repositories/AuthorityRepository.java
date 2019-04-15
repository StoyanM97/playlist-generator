package track_ninja.playlist_generator.repositories;

import org.springframework.data.repository.CrudRepository;
import track_ninja.playlist_generator.models.Authority;

public interface AuthorityRepository extends CrudRepository<Authority, Long> {
}
