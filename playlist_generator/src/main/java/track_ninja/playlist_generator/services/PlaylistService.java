package track_ninja.playlist_generator.services;
import track_ninja.playlist_generator.models.dtos.PlayListEditDTO;
import track_ninja.playlist_generator.models.dtos.PlaylistDTO;

import java.util.List;

public interface PlaylistService {
    List<PlaylistDTO> getAll();

    List<PlaylistDTO> getByGenre(String genre);

    List<PlaylistDTO> getByUser(String username);

    List<PlaylistDTO> getByTitle(String title);

    List<PlaylistDTO> getByDuration(double durationMinutes);

    PlaylistDTO getById(int id);

    boolean editPlaylist(PlayListEditDTO playListEditDTO);

    boolean deletePlaylist(int id);
}
