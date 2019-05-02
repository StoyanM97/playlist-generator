package track_ninja.playlist_generator.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import track_ninja.playlist_generator.exceptions.GenreDoesNotExistException;
import track_ninja.playlist_generator.exceptions.UserNotFoundException;
import track_ninja.playlist_generator.exceptions.NoGeneratedPlaylistsException;
import track_ninja.playlist_generator.models.dtos.PlayListEditDTO;
import track_ninja.playlist_generator.models.dtos.PlaylistDTO;
import track_ninja.playlist_generator.services.PlaylistService;

import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping("/api")
public class PlaylistController {

    private PlaylistService playlistService;

    @Autowired
    public PlaylistController(PlaylistService playlistService) {
        this.playlistService = playlistService;
    }

    @GetMapping("/playlist")
    public List<PlaylistDTO> getAll() {
        try {
            return playlistService.getAll();
        } catch (NoGeneratedPlaylistsException ex) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, ex.getMessage());
        }
    }

    @GetMapping("/playlist/filter/genre/{genre}")
    public List<PlaylistDTO> getByGenre(@PathVariable String genre) {
        try {
            return playlistService.getByGenre(genre);
        } catch (NoGeneratedPlaylistsException | GenreDoesNotExistException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        }
    }
    @GetMapping("/playlist/filter/user/{username}")
    public List<PlaylistDTO> getByUser(@PathVariable String username) {
        try {
            return playlistService.getByUser(username);
        } catch (NoGeneratedPlaylistsException | UserNotFoundException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        }
    }

    @GetMapping("/playlist/filter/{title}")
    public List<PlaylistDTO> getByTitle(@PathVariable String title) {
        try {
            return playlistService.getByTitle(title);
        } catch (NoGeneratedPlaylistsException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        }
    }

    @GetMapping("/playlist/filter/duration/{duration}")
    public List<PlaylistDTO> getByDurationBetween(@PathVariable(name = "duration") long durationMinutes) {
        try {
            return playlistService.getByDuration(durationMinutes);
        } catch (NoGeneratedPlaylistsException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        }
    }

    @GetMapping("/playlist/exist")
    public boolean playlistsExist() {
        return playlistService.playlistsExist();
    }

    @GetMapping("/playlist/{id}")
    public PlaylistDTO getById(@PathVariable int id) {
        try{
            return playlistService.getById(id);
        } catch (NoGeneratedPlaylistsException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        }
    }

    @PostMapping("/user/playlist/edit")
    public boolean editPlaylist(@RequestBody @Valid PlayListEditDTO playListEditDTO) {
        try{
            return playlistService.editPlaylist(playListEditDTO);
        } catch (NoGeneratedPlaylistsException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        }
    }

    @DeleteMapping("/user/playlist/delete/{id}")
    public boolean deletePlaylist(@PathVariable int id) {
        try{
            return playlistService.deletePlaylist(id);
        } catch (NoGeneratedPlaylistsException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        }
    }
}
