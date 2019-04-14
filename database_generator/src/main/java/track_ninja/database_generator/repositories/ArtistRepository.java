package track_ninja.database_generator.repositories;

import org.springframework.data.repository.CrudRepository;
import track_ninja.database_generator.models.Artist;

public interface ArtistRepository extends CrudRepository<Artist,Integer> {

    boolean existsByNameAndTracklist(String name, String tracklist);
    Artist getByNameAndTracklist(String name, String tracklist);
}
