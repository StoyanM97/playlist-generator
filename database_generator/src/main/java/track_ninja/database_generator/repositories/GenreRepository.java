package track_ninja.database_generator.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import track_ninja.database_generator.models.Genre;

@Repository
public interface GenreRepository extends CrudRepository< Genre, Integer> {

    boolean existsByName(String name);
    Genre getByName(String name);
}
