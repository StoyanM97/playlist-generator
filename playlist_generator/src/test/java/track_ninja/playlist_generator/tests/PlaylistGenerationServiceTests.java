package track_ninja.playlist_generator.tests;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import track_ninja.playlist_generator.duration.generator.services.LocationService;
import track_ninja.playlist_generator.exceptions.DurationTooLongException;
import track_ninja.playlist_generator.exceptions.DurationTooShortException;
import track_ninja.playlist_generator.models.Genre;
import track_ninja.playlist_generator.models.Playlist;
import track_ninja.playlist_generator.models.UserDetails;
import track_ninja.playlist_generator.models.dtos.GenreDTO;
import track_ninja.playlist_generator.models.dtos.PlaylistDTO;
import track_ninja.playlist_generator.models.dtos.PlaylistGeneratorDTO;
import track_ninja.playlist_generator.repositories.GenreRepository;
import track_ninja.playlist_generator.repositories.PlaylistRepository;
import track_ninja.playlist_generator.repositories.TrackRepository;
import track_ninja.playlist_generator.repositories.UserDetailsRepository;
import track_ninja.playlist_generator.services.PlaylistGenerationServiceImpl;

import java.util.ArrayList;
import java.util.HashMap;

import static org.mockito.ArgumentMatchers.*;

@RunWith(MockitoJUnitRunner.class)
public class PlaylistGenerationServiceTests {
    @Mock
    TrackRepository trackRepository;

    @Mock
    PlaylistRepository playlistRepository;

    @Mock
    UserDetailsRepository userDetailsRepository;

    @Mock
    LocationService locationService;

    @Mock
    GenreRepository genreRepository;

    @InjectMocks
    PlaylistGenerationServiceImpl playlistGenerationService;

    @Test(expected = DurationTooLongException.class)
    public void generatePlaylist_Should_ThrowDurationTooLongException_When_DurationOverMaxDuration() {
        String start = "testStart";
        String end = "testEnd";
        GenreDTO testGenre = new GenreDTO();
        PlaylistGeneratorDTO playlistGeneratorDTO = new PlaylistGeneratorDTO();
        playlistGeneratorDTO.setTravelFrom(start);
        playlistGeneratorDTO.setTravelTo(end);
        playlistGeneratorDTO.setGenres(new ArrayList<>());
        playlistGeneratorDTO.getGenres().add(testGenre);

        Mockito.when(locationService.getTravelDuration(start, end)).thenReturn(100000L);

        playlistGenerationService.generatePlaylist(playlistGeneratorDTO);
    }

    @Test(expected = DurationTooShortException.class)
    public void generatePlaylist_Should_ThrowDurationTooShortException_When_DurationUnder5minutes() {
        String start = "testStart";
        String end = "testEnd";
        GenreDTO testGenre = new GenreDTO();
        PlaylistGeneratorDTO playlistGeneratorDTO = new PlaylistGeneratorDTO();
        playlistGeneratorDTO.setTravelFrom(start);
        playlistGeneratorDTO.setTravelTo(end);
        playlistGeneratorDTO.setGenres(new ArrayList<>());
        playlistGeneratorDTO.getGenres().add(testGenre);

        Mockito.when(locationService.getTravelDuration(start, end)).thenReturn(4L);

        playlistGenerationService.generatePlaylist(playlistGeneratorDTO);
    }

}
