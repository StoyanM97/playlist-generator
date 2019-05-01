package track_ninja.playlist_generator.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import track_ninja.playlist_generator.exceptions.GenreDoesNotExistException;
import track_ninja.playlist_generator.exceptions.UserNotFoundException;
import track_ninja.playlist_generator.models.Playlist;
import track_ninja.playlist_generator.exceptions.NoGeneratedPlaylistsException;
import track_ninja.playlist_generator.repositories.GenreRepository;
import track_ninja.playlist_generator.repositories.PlaylistRepository;
import track_ninja.playlist_generator.repositories.UserRepository;

import java.util.List;


@Service
public class PlaylistServiceImpl implements PlaylistService{
    private static final String NO_PLAYLISTS_GENERATED_ERROR_MESSAGE = "No playlists generated!";
    private static final String NO_PLAYLISTS_GENERATED_BY_THIS_USER_ERROR_MESSAGE = "No playlists generated by this user!";
    private static final String NO_PLAYLISTS_GENERATED_WITH_SUCH_TITLE_ERROR_MESSAGE = "No playlists generated with such title!";
    private static final String NO_PLAYLISTS_GENERATED_FOR_THIS_GENRE_ERROR_MESSAGE = "No playlists generated for this genre!";
    private static final String NO_PLAYLISTS_WITH_DURATION_WITHIN_THIS_RANGE_ERROR_MESSAGE = "No playlists with duration within this range!";

    private static final String RETRIEVING_ALL_PLAYLISTS_MESSAGE = "Retrieving all playlists...";
    private static final String RETRIEVED_PLAYLISTS_MESSAGE = "Retrieved playlists!";
    private static final String RETRIEVING_ALL_PLAYLISTS_FOR_GENRE_MESSAGE = "Retrieving all playlists for genre %s...";
    private static final String COULD_NOT_RETRIEVE_PLAYLISTS_MESSAGE = "Could not retrieve playlists! %s";
    private static final String RETRIEVING_ALL_PLAYLISTS_FOR_USER_MESSAGE = "Retrieving all playlists for user %s...";
    private static final String RETRIEVING_ALL_PLAYLISTS_FOR_TITLE_MESSAGE = "Retrieving all playlists for title like %s...";
    private static final String RETRIEVING_ALL_PLAYLISTS_FOR_DURATION_MESSAGE = "Retrieving all playlists for duration between %d and %d minutes...";

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
    public List<Playlist> getAll() {
        logger.info(RETRIEVING_ALL_PLAYLISTS_MESSAGE);
        List<Playlist> playlists = playlistRepository.findAllByIsDeletedFalse();
        if (playlists.isEmpty()) {
            handleNoGeneratedPlaylistsException(NO_PLAYLISTS_GENERATED_ERROR_MESSAGE);
        }
        logger.info(RETRIEVED_PLAYLISTS_MESSAGE);
        return playlists;
    }

    @Override
    public List<Playlist> getByGenre(String genre) {
        logger.info(String.format(RETRIEVING_ALL_PLAYLISTS_FOR_GENRE_MESSAGE, genre));
        if (!genreRepository.existsByName(genre)) {
            GenreDoesNotExistException gdne = new GenreDoesNotExistException();
            logger.error(String.format(COULD_NOT_RETRIEVE_PLAYLISTS_MESSAGE, gdne.getMessage()));
            throw gdne;
        }
        List<Playlist> playlists = playlistRepository.findPlaylistsByIsDeletedFalseAndGenresContaining_Name(genre);
        if (playlists.isEmpty()) {
            handleNoGeneratedPlaylistsException(NO_PLAYLISTS_GENERATED_FOR_THIS_GENRE_ERROR_MESSAGE);
        }
        logger.info(RETRIEVED_PLAYLISTS_MESSAGE);
        return playlists;
    }

    @Override
    public List<Playlist> getByUser(String username) {
        logger.info(String.format(RETRIEVING_ALL_PLAYLISTS_FOR_USER_MESSAGE, username));
        if (userRepository.existsByUsername(username)) {
            UserNotFoundException unf = new UserNotFoundException();
            logger.error(String.format(COULD_NOT_RETRIEVE_PLAYLISTS_MESSAGE, unf.getMessage()));
            throw unf;
        }
        List<Playlist> playlists = playlistRepository.findAllByIsDeletedFalseAndUser_User_Username(username);
        if (playlists.isEmpty()) {
            handleNoGeneratedPlaylistsException(NO_PLAYLISTS_GENERATED_BY_THIS_USER_ERROR_MESSAGE);
        }
        logger.info(RETRIEVED_PLAYLISTS_MESSAGE);
        return playlists;
    }

    @Override
    public List<Playlist> getByTitle(String title) {
        logger.info(String.format(RETRIEVING_ALL_PLAYLISTS_FOR_TITLE_MESSAGE, title));
        List<Playlist> playlists = playlistRepository.findAllByIsDeletedFalseAndTitleLike(title);
        if (playlists.isEmpty()) {
            handleNoGeneratedPlaylistsException(NO_PLAYLISTS_GENERATED_WITH_SUCH_TITLE_ERROR_MESSAGE);
        }
        logger.info(RETRIEVED_PLAYLISTS_MESSAGE);
        return playlists;
    }

    @Override
    public List<Playlist> getByDurationBetween(long minDurationMinutes, long maxDurationMinutes) {
        logger.info(String.format(RETRIEVING_ALL_PLAYLISTS_FOR_DURATION_MESSAGE, minDurationMinutes, maxDurationMinutes));
        List<Playlist> playlists = playlistRepository.findAllByDurationBetween(minDurationMinutes * 60, maxDurationMinutes * 60);
        if (playlists.isEmpty()) {
            handleNoGeneratedPlaylistsException(NO_PLAYLISTS_WITH_DURATION_WITHIN_THIS_RANGE_ERROR_MESSAGE);
        }
        logger.info(RETRIEVED_PLAYLISTS_MESSAGE);
        return playlists;
    }

    private void handleNoGeneratedPlaylistsException(String noPlaylistsGeneratedErrorMessage) {
        NoGeneratedPlaylistsException ngp = new NoGeneratedPlaylistsException(noPlaylistsGeneratedErrorMessage);
        logger.error(String.format(COULD_NOT_RETRIEVE_PLAYLISTS_MESSAGE, ngp.getMessage()));
        throw ngp;
    }
}
