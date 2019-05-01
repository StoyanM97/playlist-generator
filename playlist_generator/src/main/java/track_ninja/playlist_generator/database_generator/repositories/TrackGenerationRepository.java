package track_ninja.playlist_generator.database_generator.repositories;

import org.springframework.data.repository.CrudRepository;
import track_ninja.playlist_generator.database_generator.models.Track;

public interface TrackGenerationRepository extends CrudRepository<Track, Integer> {

      Track getByTitle(String title);
      boolean existsByTrackId(int trackId);
}
