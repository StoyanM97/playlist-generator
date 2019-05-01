package track_ninja.playlist_generator.tests;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
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
}
