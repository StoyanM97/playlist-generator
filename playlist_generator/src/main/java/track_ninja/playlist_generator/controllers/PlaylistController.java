package track_ninja.playlist_generator.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import track_ninja.playlist_generator.models.Playlist;
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
        return playlistService.getAll();
    }

    @GetMapping("/filter/genre")
    public Iterable<Playlist> getByGenre(@RequestParam String genre) {
        return playlistService.getByGenre(genre);
    }

    @GetMapping("/filter/user")
    public Iterable<Playlist> getByUser(@RequestParam String username) {
        return playlistService.getByUser(username);
    }

    @GetMapping("/filter/title")
    public Iterable<Playlist> getByTitle(@RequestParam String title) {
        return playlistService.getByTitle(title);
    }
}
