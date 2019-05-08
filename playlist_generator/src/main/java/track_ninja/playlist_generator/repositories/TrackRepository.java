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
public interface TrackRepository extends CrudRepository<Track, Integer> {
    @Query(value = "select * from tracks join genres g on tracks.genre_id = g.genre_id where g.name = ?1 order by RAND() LIMIT 1", nativeQuery = true)
    Track findRandomTrackByGenre(String genre);

    @Query(value = "select * from tracks join genres g on tracks.genre_id = g.genre_id where g.name = ?1 and artist_id not in ?2 order by RAND() LIMIT 1", nativeQuery = true)
    Track findRandomTrackByGenreAndArtistNotInSet(String genre, Deque<Integer> artistsChecked);

    @Query(value = "SELECT * FROM tracks t JOIN genres g on t.genre_id = g.genre_id WHERE g.name = ?1 ORDER BY rank DESC LIMIT ?2", nativeQuery = true)
    List<Track> findTopGenre(String genre, long count);

    @Query(value = "select * from tracks join genres g on tracks.genre_id = g.genre_id where g.name = ?1 and track_id not in ?2 order by RAND() LIMIT 1", nativeQuery = true)
    Track findRandomTrackByGenreAndTrackNotInSet(String genre, Deque<Integer> tracksChecked);

    @Query(value = "SELECT * FROM tracks t JOIN genres g ON t.genre_id = g.genre_id WHERE g.name = ?1 ORDER BY rank LIMIT 1", nativeQuery = true)
    Track findTopTrackByGenre(String genre);

    @Query(value = "SELECT * FROM tracks t JOIN genres g ON t.genre_id = g.genre_id WHERE g.name = ?1 AND artist_id NOT IN ?2 ORDER BY rank DESC LIMIT 1", nativeQuery = true)
    Track findTopTrackByGenreAndArtistNotIn(String genre, Deque<Integer> artistsChecked);

    @Query(value = "SELECT * FROM tracks t JOIN genres g ON t.genre_id = g.genre_id WHERE g.name = ?1 AND track_id NOT IN ?2 ORDER BY rank DESC LIMIT 1", nativeQuery = true)
    Track findTopTrackByGenreAndTrackNotIn(String genre, Deque<Integer> tracksChecked);
}
