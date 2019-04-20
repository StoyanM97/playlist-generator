package track_ninja.playlist_generator.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import track_ninja.playlist_generator.models.Playlist;
import track_ninja.playlist_generator.repositories.PlaylistRepository;


@Service
public class PlaylistServiceImpl implements PlaylistService{
    private PlaylistRepository playlistRepository;

    @Autowired
    public PlaylistServiceImpl(PlaylistRepository playlistRepository) {
        this.playlistRepository = playlistRepository;
    }


    @Override
    public Iterable<Playlist> getAll() {
        return playlistRepository.findAll();
    }

    @Override
    public Iterable<Playlist> getByGenre(String genre) {
        return playlistRepository.findPlaylistsByGenres_name(genre);
    }

    @Override
    public Iterable<Playlist> getByUser(String username) {
        return playlistRepository.findAllByUser_username(username);
    }

    @Override
    public Iterable<Playlist> getByTitle(String title) {
        return playlistRepository.findAllByTitle(title);
    }
}
