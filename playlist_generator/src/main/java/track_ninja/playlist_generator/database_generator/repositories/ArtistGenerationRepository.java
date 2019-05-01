package track_ninja.playlist_generator.database_generator.repositories;

import org.springframework.data.repository.CrudRepository;
import track_ninja.playlist_generator.database_generator.models.Artist;

public interface ArtistGenerationRepository extends CrudRepository<Artist,Integer> {

    boolean existsByNameAndTracklist(String name, String tracklist);
    Artist getByNameAndTracklist(String name, String tracklist);
}
