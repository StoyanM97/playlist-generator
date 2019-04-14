package track_ninja.database_generator.repositories;

import org.springframework.data.repository.CrudRepository;
import track_ninja.database_generator.models.Track;

public interface TrackRepository extends CrudRepository<Track, Integer> {

      Track getByTitle(String title);
}
