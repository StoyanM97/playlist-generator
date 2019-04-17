package track_ninja.playlist_generator.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import track_ninja.playlist_generator.models.Genre;
import track_ninja.playlist_generator.models.Track;

import java.util.Deque;
import java.util.List;
import java.util.Set;

@Repository
public interface TrackRepository extends CrudRepository<Track, Long> {
    @Query(value = "select * from tracks join genres g on tracks.genre_id = g.genre_id where g.name = ?1 order by RAND() LIMIT 1", nativeQuery = true)
    Track findRandomTrackByGenre(String genre);

    @Query(value = "select * from tracks join genres g on tracks.genre_id = g.genre_id where g.name = ?1 and artist_id not in ?2 order by RAND() LIMIT 1", nativeQuery = true)
    Track findRandomTrackByGenreAndArtistNotInSet(String genre, Deque<Integer> artistsChecked);
}
