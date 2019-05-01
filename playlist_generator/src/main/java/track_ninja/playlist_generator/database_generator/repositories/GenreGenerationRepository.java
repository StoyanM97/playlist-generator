package track_ninja.playlist_generator.database_generator.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import track_ninja.playlist_generator.database_generator.models.Genre;

@Repository
public interface GenreGenerationRepository extends CrudRepository<Genre, Integer> {

    boolean existsByName(String name);
    Genre getByName(String name);
    boolean existsByGenreId(int genreId);
}
