package track_ninja.playlist_generator.database_generator.repositories;

import org.springframework.data.repository.CrudRepository;
import track_ninja.playlist_generator.database_generator.models.GenerationArtist;

public interface ArtistGenerationRepository extends CrudRepository<GenerationArtist,Integer> {

    boolean existsByNameAndTracklist(String name, String tracklist);
    GenerationArtist getByNameAndTracklist(String name, String tracklist);
}
