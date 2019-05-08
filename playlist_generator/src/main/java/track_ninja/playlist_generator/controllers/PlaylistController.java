package track_ninja.playlist_generator.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import track_ninja.playlist_generator.exceptions.GenreDoesNotExistException;
import track_ninja.playlist_generator.exceptions.PlaylistNotGeneretedByThisUserException;
import track_ninja.playlist_generator.exceptions.UserNotFoundException;
import track_ninja.playlist_generator.exceptions.NoGeneratedPlaylistException;
import track_ninja.playlist_generator.models.dtos.PlayListEditDTO;
import track_ninja.playlist_generator.models.dtos.PlaylistDTO;
import track_ninja.playlist_generator.services.PlaylistService;

import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping("/api")
public class PlaylistController {

    private static final Logger LOGGER = LoggerFactory.getLogger(PlaylistController.class);

    private PlaylistService playlistService;

    @Autowired
    public PlaylistController(PlaylistService playlistService) {
        this.playlistService = playlistService;
    }

    @GetMapping("/playlist")
    public List<PlaylistDTO> getAll() {
        try {
            return playlistService.getAll();
        } catch (NoGeneratedPlaylistException ex) {
            LOGGER.error(ex.getMessage());
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, ex.getMessage());
        }
    }

    @GetMapping("/playlist/filter/genre/{genre}")
    public List<PlaylistDTO> getByGenre(@PathVariable String genre) {
        try {
            return playlistService.getByGenre(genre);
        } catch (NoGeneratedPlaylistException | GenreDoesNotExistException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        }
    }
    @GetMapping("/playlist/filter/user/{username}")
    public List<PlaylistDTO> getByUser(@PathVariable String username) {
        try {
            return playlistService.getByUser(username);
        } catch (NoGeneratedPlaylistException | UserNotFoundException ex) {
            LOGGER.error(ex.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        }
    }

    @GetMapping("/playlist/filter/{title}")
    public List<PlaylistDTO> getByTitle(@PathVariable String title) {
        try {
            return playlistService.getByTitle(title);
        } catch (NoGeneratedPlaylistException ex) {
            LOGGER.error(ex.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        }
    }

    @GetMapping("/playlist/filter/duration/{duration}")
    public List<PlaylistDTO> getByDurationBetween(@PathVariable(name = "duration") double durationMinutes) {
        try {
            return playlistService.getByDuration(durationMinutes);
        } catch (NoGeneratedPlaylistException ex) {
            LOGGER.error(ex.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        }
    }

    @GetMapping("/playlist/{id}")
    public PlaylistDTO getById(@PathVariable int id) {
        try{
            return playlistService.getById(id);
        } catch (NoGeneratedPlaylistException ex) {
            LOGGER.error(ex.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        }
    }

    @PutMapping("/user/playlist/edit")
    public boolean editPlaylist(@RequestBody @Valid PlayListEditDTO playListEditDTO) {
        try{
            return playlistService.editPlaylist(playListEditDTO);
        } catch (NoGeneratedPlaylistException ex) {
            LOGGER.error(ex.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        } catch (PlaylistNotGeneretedByThisUserException ex) {
            LOGGER.error(ex.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, ex.getMessage());
        }
    }

    @DeleteMapping("/user/playlist/delete/{id}")
    public boolean deletePlaylist(@PathVariable int id) {
        try{
            return playlistService.deletePlaylist(id);
        } catch (NoGeneratedPlaylistException ex) {
            LOGGER.error(ex.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        }
    }
}
