package track_ninja.playlist_generator.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import track_ninja.playlist_generator.exceptions.DurationTooShortException;
import track_ninja.playlist_generator.exceptions.LocationNotFoundException;
import track_ninja.playlist_generator.models.dtos.PlaylistDTO;
import track_ninja.playlist_generator.models.dtos.PlaylistGeneratorDTO;
import track_ninja.playlist_generator.exceptions.DurationTooLongException;
import track_ninja.playlist_generator.services.PlaylistGenerationService;

import javax.validation.Valid;


@RestController
@RequestMapping("/api/user/generate")
public class PlaylistGenerationController {
    private static final Logger LOGGER = LoggerFactory.getLogger(PlaylistGenerationController.class);

    private PlaylistGenerationService playlistGenerationService;

    @Autowired
    public PlaylistGenerationController(PlaylistGenerationService playlistGenerationService) {
        this.playlistGenerationService = playlistGenerationService;
    }

    @PostMapping
    public PlaylistDTO generatePlaylist(@Valid @RequestBody PlaylistGeneratorDTO playlistGeneratorDTO){
        try {
            return playlistGenerationService.generatePlaylist(playlistGeneratorDTO);
        } catch (DurationTooLongException | DurationTooShortException | LocationNotFoundException ex) {
            LOGGER.error(ex.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, ex.getMessage());
        }
    }
}
