package track_ninja.playlist_generator.services;

import track_ninja.playlist_generator.models.Playlist;

public interface PlaylistService {
    Iterable<Playlist> getAll();

    Iterable<Playlist> getByGenre(String genre);
}
