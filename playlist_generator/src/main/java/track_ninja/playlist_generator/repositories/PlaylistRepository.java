package track_ninja.playlist_generator.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import track_ninja.playlist_generator.models.Playlist;

import java.util.List;

@Repository
public interface PlaylistRepository extends CrudRepository<Playlist, Integer> {
//    List<Playlist> findPlaylistsByIsDeletedFalseAndGenresContaining_Name(String genre);

    List<Playlist> findAllByIsDeletedFalseAndUser_User_Username(String username);

    List<Playlist> findAllByIsDeletedFalseAndTitle(String title);
}
