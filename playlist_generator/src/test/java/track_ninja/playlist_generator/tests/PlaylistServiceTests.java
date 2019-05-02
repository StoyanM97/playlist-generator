package track_ninja.playlist_generator.tests;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import track_ninja.playlist_generator.exceptions.GenreDoesNotExistException;
import track_ninja.playlist_generator.exceptions.NoGeneratedPlaylistsException;
import track_ninja.playlist_generator.exceptions.UserNotFoundException;
import track_ninja.playlist_generator.models.dtos.PlayListEditDTO;
import track_ninja.playlist_generator.repositories.GenreRepository;
import track_ninja.playlist_generator.repositories.PlaylistRepository;
import track_ninja.playlist_generator.repositories.UserRepository;
import track_ninja.playlist_generator.services.PlaylistServiceImpl;

import java.util.ArrayList;

@RunWith(MockitoJUnitRunner.class)
public class PlaylistServiceTests {
    @Mock
    PlaylistRepository playlistRepository;

    @Mock
    GenreRepository genreRepository;

    @Mock
    UserRepository userRepository;

    @InjectMocks
    PlaylistServiceImpl playlistService;

    @Test(expected = NoGeneratedPlaylistsException.class)
    public void getAll_Should_ThrowNoGeneratedPlaylistException_When_NoPlaylistGenerated() {
        Mockito.when(playlistRepository.findAll()).thenReturn(new ArrayList<>());

        playlistService.getAll();
    }

    @Test(expected = GenreDoesNotExistException.class)
    public void getByGenre_Should_ThrowGenreDoesNotExistException_When_GenreDoesNotExist() {
        String genre = "testGenre";

        Mockito.when(genreRepository.existsByName(genre)).thenReturn(false);

        playlistService.getByGenre(genre);
    }

    @Test(expected = NoGeneratedPlaylistsException.class)
    public void getByGenre_Should_ThrowNoGeneratedPlaylistsException_When_NoPlaylistsForThisGenre() {
        String genre = "testGenre";

        Mockito.when(genreRepository.existsByName(genre)).thenReturn(true);
        Mockito.when(playlistRepository.findPlaylistsByIsDeletedFalseAndGenresContaining_Name(genre)).thenReturn(new ArrayList<>());

        playlistService.getByGenre(genre);
    }

    @Test(expected = UserNotFoundException.class)
    public void getByUser_Should_ThrowUserNotFoundException_When_UserDoesNotExist() {
        String user = "testUser";

        Mockito.when(userRepository.existsByUsername(user)).thenReturn(false);

        playlistService.getByUser(user);
    }

    @Test(expected = NoGeneratedPlaylistsException.class)
    public void getByUser_Should_ThrowNoGeneratedPlaylistsException_When_NoPlaylistsForThisUser() {
        String user = "testUser";

        Mockito.when(userRepository.existsByUsername(user)).thenReturn(true);
        Mockito.when(playlistRepository.findAllByIsDeletedFalseAndUser_User_Username(user)).thenReturn(new ArrayList<>());

        playlistService.getByUser(user);
    }


    @Test(expected = NoGeneratedPlaylistsException.class)
    public void getByTitle_Should_ThrowNoGeneratedPlaylistsException_When_NoPlaylistsWithThisTitle() {
        String title = "testTitle";

        Mockito.when(playlistRepository.findAllByIsDeletedFalseAndTitleLike(title)).thenReturn(new ArrayList<>());

        playlistService.getByTitle(title);
    }

    @Test(expected = NoGeneratedPlaylistsException.class)
    public void getByDuration_Should_ThrowNoGeneratedPlaylistsException_When_NoPlaylistsWithThisDuration() {
        long duration = 0L;

        Mockito.when(playlistRepository.findAllByIsDeletedFalseAndDurationBetween(duration - 600, duration + 600)).thenReturn(new ArrayList<>());

        playlistService.getByDuration(duration);
    }

    @Test(expected = NoGeneratedPlaylistsException.class)
    public void getById_Should_ThrowNoGeneratedPlaylistsException_When_NoPlaylistsWithThisId() {
        int id = 0;

        Mockito.when(playlistRepository.findByIsDeletedFalseAndPlaylistId(id)).thenReturn(null);

        playlistService.getById(0);
    }

    @Test(expected = NoGeneratedPlaylistsException.class)
    public void editPlaylist_Should_ThrowNoGeneratedPlaylistsException_When_NoPlaylistsWithThisId() {
        PlayListEditDTO playlistDTO = new PlayListEditDTO();
        playlistDTO.setPlaylistId(0);

        Mockito.when(playlistRepository.findByIsDeletedFalseAndPlaylistId(0)).thenReturn(null);

        playlistService.editPlaylist(playlistDTO);
    }

    @Test(expected = NoGeneratedPlaylistsException.class)
    public void deletePlaylist_Should_ThrowNoGeneratedPlaylistsException_When_NoPlaylistsWithThisId() {
        int id = 0;

        Mockito.when(playlistRepository.findByIsDeletedFalseAndPlaylistId(id)).thenReturn(null);

        playlistService.deletePlaylist(id);
    }
}
