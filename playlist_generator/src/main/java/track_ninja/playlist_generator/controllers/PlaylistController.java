package track_ninja.playlist_generator.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import track_ninja.playlist_generator.models.Playlist;
import track_ninja.playlist_generator.models.exceptions.NoGeneratedPlaylistsException;
import track_ninja.playlist_generator.services.PlaylistService;


@RestController
@RequestMapping("/api")
public class PlaylistController {

    private PlaylistService playlistService;

    @Autowired
    public PlaylistController(PlaylistService playlistService) {
        this.playlistService = playlistService;
    }

    @GetMapping
    public Iterable<Playlist> getAll() {
        try {
            return playlistService.getAll();
        } catch (NoGeneratedPlaylistsException ex) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, ex.getMessage());
        }
    }

    @GetMapping("/filter/{genre}")
    public Iterable<Playlist> getByGenre(@PathVariable String genre) {
        return playlistService.getByGenre(genre);
    }
    @GetMapping("/filter/user")
    public Iterable<Playlist> getByUser(@PathVariable String username) {
        try {
            return playlistService.getByUser(username);
        } catch (NoGeneratedPlaylistsException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        }
    }

    @GetMapping("/filter/{title}")
    public Iterable<Playlist> getByTitle(@PathVariable String title) {
        try {
            return playlistService.getByTitle(title);
        } catch (NoGeneratedPlaylistsException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        }
    }
}
