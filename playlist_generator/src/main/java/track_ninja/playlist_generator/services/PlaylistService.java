package track_ninja.playlist_generator.services;

import track_ninja.playlist_generator.models.Playlist;
import track_ninja.playlist_generator.models.dtos.PlayListEditDTO;

import java.util.List;

public interface PlaylistService {
    List<Playlist> getAll();

    List<Playlist> getByGenre(String genre);

    List<Playlist> getByUser(String username);

    List<Playlist> getByTitle(String title);

    List<Playlist> getByDurationBetween(long minDurationMinutes, long maxDurationMinutes);

    boolean playlistsExist();

    Playlist getById(int id);

    boolean editPlaylist(PlayListEditDTO playListEditDTO);

    boolean deletePlaylist(int id);
}
