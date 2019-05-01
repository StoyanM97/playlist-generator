package track_ninja.playlist_generator.database_generator.repositories;

import org.springframework.data.repository.CrudRepository;
import track_ninja.playlist_generator.database_generator.models.GenerationAlbum;

public interface AlbumGenerationRepository extends CrudRepository<GenerationAlbum, Integer> {

      boolean existsByTitleAndTracklist(String title, String tracklist);
      GenerationAlbum getByTitleAndTracklist(String title, String tracklist);
}
