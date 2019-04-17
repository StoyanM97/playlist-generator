package track_ninja.playlist_generator.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import track_ninja.playlist_generator.models.Track;
import track_ninja.playlist_generator.services.PlaylistGenerationService;

import java.util.Deque;

@RestController
@RequestMapping("/api/user/generatePlaylist")
public class PlaylistGenerationController {
    private PlaylistGenerationService playlistGenerationService;

    @Autowired
    public PlaylistGenerationController(PlaylistGenerationService playlistGenerationService) {
        this.playlistGenerationService = playlistGenerationService;
    }

    @GetMapping
    public Deque<Track> findRandomByGenre(@RequestParam String genre){
        return playlistGenerationService.generatePlaylist(genre, 1000L, "firstPlaylist");
    }
}
