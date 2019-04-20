package track_ninja.playlist_generator.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import track_ninja.playlist_generator.models.Playlist;

import java.util.List;

@Repository
public interface PlaylistRepository extends CrudRepository<Playlist, Long> {
    List<Playlist> findPlaylistsByGenres_name(String genre);

    List<Playlist> findAllByUser_username(String username);

    List<Playlist> findAllByTitle(String title);
}
