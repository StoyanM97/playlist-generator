package track_ninja.playlist_generator.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import track_ninja.playlist_generator.models.Playlist;

import java.util.List;

@Repository
public interface PlaylistRepository extends CrudRepository<Playlist, Integer> {

    @Query(value = "SELECT * FROM playlists p " +
            "JOIN genre_playlist_relations gpr ON p.playlist_id = gpr.playlist_id " +
            "JOIN  genres g ON gpr.genre_id = g.genre_id " +
            "WHERE g.name = ?1",
            nativeQuery = true)
    List<Playlist> findPlaylistsByIsDeletedFalseAndGenresContaining_Name(String genre);

    @Query(value = "SELECT * FROM playlists p " +
            "JOIN playlist_track_relations ptr ON p.playlist_id = ptr.playlist_id " +
            "JOIN tracks t ON ptr.track_id = t.track_id " +
            "WHERE p.is_deleted = 0 " +
            "GROUP BY p.playlist_id " +
            "ORDER BY AVG(t.rank) DESC", nativeQuery = true)
    List<Playlist> findAllByIsDeletedFalse();

    List<Playlist> findAllByIsDeletedFalseAndUser_User_UsernameLike(String username);

    List<Playlist> findAllByIsDeletedFalseAndTitleLike(String title);

    List<Playlist> findAllByIsDeletedFalseAndDurationBetween(long start, long end);

    Playlist findByIsDeletedFalseAndPlaylistId(int id);
}
