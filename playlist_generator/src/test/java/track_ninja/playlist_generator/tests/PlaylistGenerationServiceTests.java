package track_ninja.playlist_generator.tests;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import track_ninja.playlist_generator.duration.generator.services.LocationService;
import track_ninja.playlist_generator.models.Genre;
import track_ninja.playlist_generator.models.Playlist;
import track_ninja.playlist_generator.models.UserDetails;
import track_ninja.playlist_generator.models.dtos.PlaylistDTO;
import track_ninja.playlist_generator.models.dtos.PlaylistGeneratorDTO;
import track_ninja.playlist_generator.repositories.GenreRepository;
import track_ninja.playlist_generator.repositories.PlaylistRepository;
import track_ninja.playlist_generator.repositories.TrackRepository;
import track_ninja.playlist_generator.repositories.UserDetailsRepository;
import track_ninja.playlist_generator.services.PlaylistGenerationServiceImpl;

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

//    @Test
//    public void generatePlaylist_Should_NorRepeatArtists_When_AllowSameArtistIsFalse() {
//        PlaylistGeneratorDTO playlistGeneratorDTO = new PlaylistGeneratorDTO();
//        playlistGeneratorDTO.setTitle("TestPlaylist");
//        playlistGeneratorDTO.setUsername("TestUser");
//        playlistGeneratorDTO.setTravelFrom("TestDestination1");
//        playlistGeneratorDTO.setTravelTo("TestDestination2");
//        playlistGeneratorDTO.setAllowSameArtists(false);
//        playlistGeneratorDTO.setUseTopTracks(false);
//        playlistGeneratorDTO.setGenres(new HashMap<>());
//        playlistGeneratorDTO.getGenres().put("pop", 100);
//
//        Mockito.when(locationService.getTravelDuration("TestDestination1", "TestDestination2")).thenReturn(6000L);
//        Mockito.when(userDetailsRepository.findByIsDeletedFalseAndUser_Username("TestUser")).thenReturn(new UserDetails());
//        Mockito.when(genreRepository.findByName("pop")).thenReturn(new Genre());
//        Mockito.when(trackRepository.findRandomTrackByGenre("pop")).thenCallRealMethod();
//        Mockito.when(playlistRepository.save(any(Playlist.class))).thenReturn(null);
//        Mockito.when(genreRepository.save(any(Genre.class))).thenReturn(null);
//
//        PlaylistDTO result = playlistGenerationService.generatePlaylist(playlistGeneratorDTO);
//
//        Assert.assertTrue(result.getDuration() <= 6300L && result.getDuration() >= 5700L);
//    }
}
