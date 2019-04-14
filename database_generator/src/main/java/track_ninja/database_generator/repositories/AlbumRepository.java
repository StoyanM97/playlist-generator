package track_ninja.database_generator.repositories;

import org.springframework.data.repository.CrudRepository;
import track_ninja.database_generator.models.Album;

public interface AlbumRepository extends CrudRepository<Album, Integer> {

      boolean existsByTitleAndTracklist(String title, String tracklist);
      Album getByTitleAndTracklist(String title, String tracklist);
}
