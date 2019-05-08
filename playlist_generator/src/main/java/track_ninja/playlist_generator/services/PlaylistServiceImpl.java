package track_ninja.playlist_generator.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import track_ninja.playlist_generator.exceptions.GenreDoesNotExistException;
import track_ninja.playlist_generator.exceptions.PlaylistNotGeneretedByThisUserException;
import track_ninja.playlist_generator.exceptions.UserNotFoundException;
import track_ninja.playlist_generator.models.Playlist;
import track_ninja.playlist_generator.exceptions.NoGeneratedPlaylistException;
import track_ninja.playlist_generator.models.dtos.PlayListEditDTO;
import track_ninja.playlist_generator.models.dtos.PlaylistDTO;
import track_ninja.playlist_generator.models.mappers.ModelMapper;
import track_ninja.playlist_generator.repositories.GenreRepository;
import track_ninja.playlist_generator.repositories.PlaylistRepository;
import track_ninja.playlist_generator.repositories.UserRepository;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class PlaylistServiceImpl implements PlaylistService{
    private static final String RETRIEVING_ALL_PLAYLISTS_MESSAGE = "Retrieving all playlists...";
    private static final String RETRIEVED_PLAYLISTS_MESSAGE = "Retrieved playlists!";
    private static final String RETRIEVING_ALL_PLAYLISTS_FOR_GENRE_MESSAGE = "Retrieving all playlists for genre %s...";
    private static final String RETRIEVING_ALL_PLAYLISTS_FOR_USER_MESSAGE = "Retrieving all playlists for user %s...";
    private static final String RETRIEVING_ALL_PLAYLISTS_FOR_TITLE_MESSAGE = "Retrieving all playlists for title like %s...";
    private static final String RETRIEVING_ALL_PLAYLISTS_FOR_DURATION_MESSAGE = "Retrieving all playlists for duration around %d minutes...";

    private static final String LOOKING_FOR_PLAYLIST_WITH_ID_MESSAGE = "Looking for playlist with id %d...";
    private static final String PLAYLIST_FOUND_MESSAGE = "Playlist found!";
    private static final String PLAYLIST_EDITED_MESSAGE = "Playlist edited!";
    private static final String PLAYLIST_DELETED_MESSAGE = "Playlist deleted!";
    private static final String EDITING_PLAYLIST_MESSAGE = "Editing playlist with id %d...";
    private static final String DELETING_PLAYLIST_MESSAGE = "Deleting playlist with id %d...";
    private static final String MATCHER = "%";
    private static final String ROLE_ADMIN = "ROLE_ADMIN";

    private static final Logger logger = LoggerFactory.getLogger(PlaylistService.class);

    private PlaylistRepository playlistRepository;
    private GenreRepository genreRepository;
    private UserRepository userRepository;

    @Autowired
    public PlaylistServiceImpl(PlaylistRepository playlistRepository, GenreRepository genreRepository, UserRepository userRepository) {
        this.playlistRepository = playlistRepository;
        this.genreRepository = genreRepository;
        this.userRepository = userRepository;
    }


    @Override
    public List<PlaylistDTO> getAll() {
        logger.info(RETRIEVING_ALL_PLAYLISTS_MESSAGE);
        List<Playlist> playlists = playlistRepository.findAllByIsDeletedFalse();
        logger.info(RETRIEVED_PLAYLISTS_MESSAGE);
        return mapListOfPlaylistToDTOs(playlists);
    }

    @Override
    public List<PlaylistDTO> getByGenre(String genre) {
        logger.info(String.format(RETRIEVING_ALL_PLAYLISTS_FOR_GENRE_MESSAGE, genre));
        if (!genreRepository.existsByName(genre)) {
            throw new GenreDoesNotExistException();
        }
        List<Playlist> playlists = playlistRepository.findPlaylistsByIsDeletedFalseAndGenresContaining_Name(genre);
        logger.info(RETRIEVED_PLAYLISTS_MESSAGE);
        return mapListOfPlaylistToDTOs(playlists);
    }

    @Override
    public List<PlaylistDTO> getByUser(String username) {
        logger.info(String.format(RETRIEVING_ALL_PLAYLISTS_FOR_USER_MESSAGE, username));
        if (!userRepository.existsByUsername(username)) {
            throw new UserNotFoundException();
        }
        List<Playlist> playlists = playlistRepository.findAllByIsDeletedFalseAndUser_User_UsernameLike(MATCHER + username + MATCHER);
        logger.info(RETRIEVED_PLAYLISTS_MESSAGE);
        return mapListOfPlaylistToDTOs(playlists);
    }

    @Override
    public List<PlaylistDTO> getByTitle(String title) {
        logger.info(String.format(RETRIEVING_ALL_PLAYLISTS_FOR_TITLE_MESSAGE, title));
        List<Playlist> playlists = playlistRepository.findAllByIsDeletedFalseAndTitleLike(MATCHER + title + MATCHER);
        logger.info(RETRIEVED_PLAYLISTS_MESSAGE);
        return mapListOfPlaylistToDTOs(playlists);
    }

    @Override
    public List<PlaylistDTO> getByDuration(double durationMinutes) {
        long durationMinutes1 = (long) Math.ceil(durationMinutes);
        logger.info(String.format(RETRIEVING_ALL_PLAYLISTS_FOR_DURATION_MESSAGE, durationMinutes1));
        long durationSeconds = durationMinutes1 * 60;
        List<Playlist> playlists = playlistRepository.findAllByIsDeletedFalseAndDurationBetween(durationSeconds - 600, durationSeconds + 600);
        logger.info(RETRIEVED_PLAYLISTS_MESSAGE);
        return mapListOfPlaylistToDTOs(playlists);
    }

    @Override
    public PlaylistDTO getById(int id) {
        logger.info(String.format(LOOKING_FOR_PLAYLIST_WITH_ID_MESSAGE, id));
        Playlist playlist = playlistRepository.findByIsDeletedFalseAndPlaylistId(id);
        if (playlist == null) {
            throw new NoGeneratedPlaylistException();
        }
        logger.info(PLAYLIST_FOUND_MESSAGE);
        return ModelMapper.playlistToDTO(playlist);
    }

    @Override
    public boolean editPlaylist(PlayListEditDTO playListEditDTO) {
        logger.info(String.format(EDITING_PLAYLIST_MESSAGE, playListEditDTO.getPlaylistId()));
        Playlist playlist = playlistRepository.findByIsDeletedFalseAndPlaylistId(playListEditDTO.getPlaylistId());
        if (playlist == null) {
            throw new NoGeneratedPlaylistException();
        } if (!playListEditDTO.getUsername().equals(playlist.getUser().getUser().getUsername()) &&
                !userRepository.findByUsernameAndEnabledTrue(playListEditDTO.getUsername()).getAuthority().getName().toString().equals(ROLE_ADMIN)) {
            throw new PlaylistNotGeneretedByThisUserException();
        }
        playlist.setTitle(playListEditDTO.getTitle());
        playlistRepository.save(playlist);
        logger.info(PLAYLIST_EDITED_MESSAGE);
        return true;
    }

    @Override
    public boolean deletePlaylist(int id) {
        logger.info(String.format(DELETING_PLAYLIST_MESSAGE, id));
        Playlist playlist = playlistRepository.findByIsDeletedFalseAndPlaylistId(id);
        if (playlist == null) {
            throw new NoGeneratedPlaylistException();
        }
        playlist.setDeleted(true);
        playlistRepository.save(playlist);
        logger.info(PLAYLIST_DELETED_MESSAGE);
        return false;
    }

    private List<PlaylistDTO> mapListOfPlaylistToDTOs(List<Playlist> playlists) {
        return playlists.stream().map(ModelMapper::playlistToDTO).collect(Collectors.toList());
    }
}
