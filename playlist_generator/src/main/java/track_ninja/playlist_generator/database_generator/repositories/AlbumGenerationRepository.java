package track_ninja.playlist_generator.database_generator.repositories;

import org.springframework.data.repository.CrudRepository;
import track_ninja.playlist_generator.database_generator.models.Album;

public interface AlbumGenerationRepository extends CrudRepository<Album, Integer> {

      boolean existsByTitleAndTracklist(String title, String tracklist);
      Album getByTitleAndTracklist(String title, String tracklist);
}
