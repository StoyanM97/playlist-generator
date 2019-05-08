package track_ninja.playlist_generator.tests;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import track_ninja.playlist_generator.exceptions.GenreDoesNotExistException;
import track_ninja.playlist_generator.exceptions.NoGeneratedPlaylistException;
import track_ninja.playlist_generator.exceptions.PlaylistNotGeneretedByThisUserException;
import track_ninja.playlist_generator.exceptions.UserNotFoundException;
import track_ninja.playlist_generator.models.Authority;
import track_ninja.playlist_generator.models.Playlist;
import track_ninja.playlist_generator.models.User;
import track_ninja.playlist_generator.models.UserDetails;
import track_ninja.playlist_generator.models.commons.UserRole;
import track_ninja.playlist_generator.models.dtos.PlayListEditDTO;
import track_ninja.playlist_generator.repositories.GenreRepository;
import track_ninja.playlist_generator.repositories.PlaylistRepository;
import track_ninja.playlist_generator.repositories.UserRepository;
import track_ninja.playlist_generator.services.PlaylistServiceImpl;


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



    @Test(expected = GenreDoesNotExistException.class)
    public void getByGenre_Should_ThrowGenreDoesNotExistException_When_GenreDoesNotExist() {
        String genre = "testGenre";

        Mockito.when(genreRepository.existsByName(genre)).thenReturn(false);

        playlistService.getByGenre(genre);
    }


    @Test(expected = UserNotFoundException.class)
    public void getByUser_Should_ThrowUserNotFoundException_When_UserDoesNotExist() {
        String user = "testUser";

        Mockito.when(userRepository.existsByUsername(user)).thenReturn(false);

        playlistService.getByUser(user);
    }



    @Test(expected = NoGeneratedPlaylistException.class)
    public void getById_Should_ThrowNoGeneratedPlaylistsException_When_NoPlaylistsWithThisId() {
        int id = 0;

        Mockito.when(playlistRepository.findByIsDeletedFalseAndPlaylistId(id)).thenReturn(null);

        playlistService.getById(0);
    }

    @Test(expected = NoGeneratedPlaylistException.class)
    public void editPlaylist_Should_ThrowNoGeneratedPlaylistsException_When_NoPlaylistsWithThisId() {
        PlayListEditDTO playlistDTO = new PlayListEditDTO();
        playlistDTO.setPlaylistId(0);

        Mockito.when(playlistRepository.findByIsDeletedFalseAndPlaylistId(0)).thenReturn(null);

        playlistService.editPlaylist(playlistDTO);
    }

    @Test(expected = PlaylistNotGeneretedByThisUserException.class)
    public void editPlaylist_Should_ThrowPlaylistNotGeneratedByThisUserException_When_PlaylistNotCreatedByThisUser() {
        PlayListEditDTO playlistDTO = new PlayListEditDTO();
        playlistDTO.setPlaylistId(0);
        playlistDTO.setUsername("testUser");

        Authority authority = new Authority();
        authority.setName(UserRole.ROLE_USER);
        User user = new User();
        user.setUsername("differentUser");
        user.setAuthority(authority);
        UserDetails userDetails = new UserDetails();
        userDetails.setUser(user);
        Playlist playlist = new Playlist();
        playlist.setUser(userDetails);

        Mockito.when(playlistRepository.findByIsDeletedFalseAndPlaylistId(0)).thenReturn(playlist);
        Mockito.when(userRepository.findByUsernameAndEnabledTrue(playlistDTO.getUsername())).thenReturn(user);

        playlistService.editPlaylist(playlistDTO);
    }

    @Test(expected = NoGeneratedPlaylistException.class)
    public void deletePlaylist_Should_ThrowNoGeneratedPlaylistsException_When_NoPlaylistsWithThisId() {
        int id = 0;

        Mockito.when(playlistRepository.findByIsDeletedFalseAndPlaylistId(id)).thenReturn(null);

        playlistService.deletePlaylist(id);
    }
}
