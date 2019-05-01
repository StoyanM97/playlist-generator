package track_ninja.playlist_generator.database_generator.repositories;

import org.springframework.data.repository.CrudRepository;
import track_ninja.playlist_generator.database_generator.models.GenerationTrack;

public interface TrackGenerationRepository extends CrudRepository<GenerationTrack, Integer> {

      GenerationTrack getByTitle(String title);
      boolean existsByTrackId(int trackId);
}
