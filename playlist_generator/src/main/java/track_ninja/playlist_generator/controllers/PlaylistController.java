package track_ninja.playlist_generator.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
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

//    @GetMapping("/filter/genre")
//    public Iterable<Playlist> getByGenre(@RequestBody String genre) {
//        return playlistService.getByGenre(genre);
//    }

    @GetMapping("/filter/user")
    public Iterable<Playlist> getByUser(@RequestBody String username) {
        return playlistService.getByUser(username);
    }

    @GetMapping("/filter/title")
    public Iterable<Playlist> getByTitle(@RequestBody String title) {
        return playlistService.getByTitle(title);
    }
}
