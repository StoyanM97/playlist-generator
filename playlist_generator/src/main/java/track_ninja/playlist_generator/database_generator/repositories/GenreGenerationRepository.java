package track_ninja.playlist_generator.database_generator.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import track_ninja.playlist_generator.database_generator.models.GenerationGenre;

@Repository
public interface GenreGenerationRepository extends CrudRepository<GenerationGenre, Integer> {

    boolean existsByName(String name);
    GenerationGenre getByName(String name);
    boolean existsByGenreId(int genreId);
}
