package track_ninja.playlist_generator.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import track_ninja.playlist_generator.models.dtos.PlaylistDTO;
import track_ninja.playlist_generator.models.dtos.PlaylistGenerationDTO;
import track_ninja.playlist_generator.services.PlaylistGenerationService;


@RestController
@RequestMapping("/api/user/generatePlaylist")
public class PlaylistGenerationController {
    private PlaylistGenerationService playlistGenerationService;

    @Autowired
    public PlaylistGenerationController(PlaylistGenerationService playlistGenerationService) {
        this.playlistGenerationService = playlistGenerationService;
    }

    @PostMapping
    public PlaylistDTO findRandomByGenre(@RequestBody PlaylistGenerationDTO playlistGenerationDTO){
        return playlistGenerationService.generatePlaylist(playlistGenerationDTO);
    }
}
