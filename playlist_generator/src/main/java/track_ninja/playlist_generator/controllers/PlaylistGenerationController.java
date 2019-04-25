package track_ninja.playlist_generator.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import track_ninja.playlist_generator.models.dtos.PlaylistDTO;
import track_ninja.playlist_generator.models.dtos.PlaylistGeneratorDTO;
import track_ninja.playlist_generator.services.PlaylistGenerationService;

import javax.validation.Valid;


@RestController
@RequestMapping("/api/user/generatePlaylist")
public class PlaylistGenerationController {
    private PlaylistGenerationService playlistGenerationService;

    @Autowired
    public PlaylistGenerationController(PlaylistGenerationService playlistGenerationService) {
        this.playlistGenerationService = playlistGenerationService;
    }

    @PostMapping
    public PlaylistDTO findRandomByGenre(@Valid @RequestBody PlaylistGeneratorDTO playlistGeneratorDTO){
        return playlistGenerationService.generatePlaylist(playlistGeneratorDTO);
    }
}
