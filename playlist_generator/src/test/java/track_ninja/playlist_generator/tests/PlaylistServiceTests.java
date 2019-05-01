package track_ninja.playlist_generator.tests;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import track_ninja.playlist_generator.exceptions.GenreDoesNotExistException;
import track_ninja.playlist_generator.exceptions.NoGeneratedPlaylistsException;
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
}
