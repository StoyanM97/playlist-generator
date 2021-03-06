package track_ninja.playlist_generator.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import track_ninja.playlist_generator.duration.generator.services.LocationService;
import track_ninja.playlist_generator.exceptions.DurationTooShortException;
import track_ninja.playlist_generator.models.*;
import track_ninja.playlist_generator.models.dtos.GenreDTO;
import track_ninja.playlist_generator.models.dtos.PlaylistDTO;
import track_ninja.playlist_generator.models.dtos.PlaylistGeneratorDTO;
import track_ninja.playlist_generator.exceptions.DurationTooLongException;
import track_ninja.playlist_generator.models.mappers.ModelMapper;
import track_ninja.playlist_generator.repositories.GenreRepository;
import track_ninja.playlist_generator.repositories.PlaylistRepository;
import track_ninja.playlist_generator.repositories.TrackRepository;
import track_ninja.playlist_generator.repositories.UserDetailsRepository;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class PlaylistGenerationServiceImpl implements PlaylistGenerationService {

    private static final String INITIATED_PLAYLIST_GENERATION_MESSAGE = "Initiated playlist generation. Parameters: %s";
    private static final String PLAYLIST_SAVED_MESSAGE = "playlist successfully saved! Total duration: %d seconds; Total number of tracks: %d";
    private static final String PLAYLIST_ADDED_TO_GENRES_MESSAGE = "playlist successfully added to all of it's genres!";

    private static final Logger logger = LoggerFactory.getLogger(PlaylistGenerationService.class);
    private static final int MAX_DURATION = 500000;
    private static final int FIVE_MINUTES_AS_SECONDS = 300;
    private static final int ONE_MINUTE_AS_SECONDS = 60;

    private TrackRepository trackRepository;
    private PlaylistRepository playlistRepository;
    private UserDetailsRepository userDetailsRepository;
    private LocationService locationService;
    private GenreRepository genreRepository;

    @Autowired
    public PlaylistGenerationServiceImpl(TrackRepository trackRepository, PlaylistRepository playlistRepository,
                                         UserDetailsRepository userDetailsRepository, LocationService locationService,
                                         GenreRepository genreRepository) {
        this.trackRepository = trackRepository;
        this.playlistRepository = playlistRepository;
        this.userDetailsRepository = userDetailsRepository;
        this.locationService = locationService;
        this.genreRepository = genreRepository;
    }

    @Override
    public PlaylistDTO generatePlaylist(PlaylistGeneratorDTO playlistGeneratorDTO) {
        logger.info(String.format(INITIATED_PLAYLIST_GENERATION_MESSAGE, playlistGeneratorDTO.toString()));

        long totalDuration = locationService.getTravelDuration(playlistGeneratorDTO.getTravelFrom(), playlistGeneratorDTO.getTravelTo()) * ONE_MINUTE_AS_SECONDS;

        if (totalDuration > MAX_DURATION) {
            throw new DurationTooLongException();
        } else if (totalDuration < FIVE_MINUTES_AS_SECONDS) {
            throw new DurationTooShortException();
        }

        Deque<Track> tracks = new ArrayDeque<>();
        Playlist generatedPlaylist = new Playlist();
        generatedPlaylist.setTitle(playlistGeneratorDTO.getTitle());
        generatedPlaylist.setUser(userDetailsRepository.findByIsDeletedFalseAndUser_Username(playlistGeneratorDTO.getUsername()));
        generatedPlaylist.setDeleted(false);
        generatedPlaylist.setDuration(0L);
        List<Genre> genres = new ArrayList<>();
        for (GenreDTO genre : playlistGeneratorDTO.getGenres()) {
            genres.add(genreRepository.findByName(genre.getGenre()));
        }
        generatedPlaylist.setGenres(genres.stream().filter(Objects::nonNull).collect(Collectors.toSet()));
        int errorMargin = FIVE_MINUTES_AS_SECONDS / generatedPlaylist.getGenres().size();
        long topGenreDuration = 0L;
        String topGenre = "";
        long durationSeconds = 0L;
        if (playlistGeneratorDTO.isAllowSameArtists()) {
            if (playlistGeneratorDTO.isUseTopTracks()) {
                Deque<Integer> trackIds = new ArrayDeque<>();
                for (GenreDTO genre : playlistGeneratorDTO.getGenres()) {
                    if (genre.getPercentage() == 0) {
                        continue;
                    }
                    double genreDurationSecond = getGenreDurationSecond(totalDuration, genre);
                    durationSeconds = updateDurationSecondsAllowSameArtist(tracks, trackIds, genre, durationSeconds);

                    while (durationSeconds < genreDurationSecond - errorMargin) {
                        durationSeconds = addTrackInLoopAllowSameArtistAndTopTracks(tracks, trackIds, durationSeconds, genre.getGenre());
                    }
                    generatedPlaylist.setDuration(generatedPlaylist.getDuration() + durationSeconds);
                    if (durationSeconds > topGenreDuration) {
                        topGenreDuration = durationSeconds;
                        topGenre = genre.getGenre();
                    }
                }
            } else {
                Deque<Integer> trackIds = new ArrayDeque<>();
                for (GenreDTO genre : playlistGeneratorDTO.getGenres()) {
                    if (genre.getPercentage() == 0) {
                        continue;
                    }
                    double genreDurationSecond = getGenreDurationSecond(totalDuration, genre);
                    durationSeconds = updateDurationSecondsAllowSameArtist(tracks, trackIds, genre, durationSeconds);

                    while (durationSeconds < genreDurationSecond - errorMargin) {
                        durationSeconds = addTrackInLoopAllowSameArtist(tracks, trackIds, durationSeconds, genre.getGenre());
                    }
                    generatedPlaylist.setDuration(generatedPlaylist.getDuration() + durationSeconds);
                    if (durationSeconds > topGenreDuration) {
                        topGenreDuration = durationSeconds;
                        topGenre = genre.getGenre();
                    }
                }
            }
        } else if (playlistGeneratorDTO.isUseTopTracks()) {
            Deque<Integer> artistIds = new ArrayDeque<>();
            for (GenreDTO genre : playlistGeneratorDTO.getGenres()) {
                if (genre.getPercentage() == 0) {
                    continue;
                }
                double genreDurationSecond = getGenreDurationSecond(totalDuration, genre);
                Track firstTrack = trackRepository.findTopTrackByGenre(genre.getGenre());
                artistIds.add(firstTrack.getArtist().getArtistId());
                tracks.add(firstTrack);
                durationSeconds = updateDuration(durationSeconds, firstTrack);

                while (durationSeconds < genreDurationSecond - errorMargin) {
                    durationSeconds = addTrackInLoopUseTopTracks(tracks, artistIds, durationSeconds, genre.getGenre());
                }
                generatedPlaylist.setDuration(generatedPlaylist.getDuration() + durationSeconds);
                if (durationSeconds > topGenreDuration) {
                    topGenreDuration = durationSeconds;
                    topGenre = genre.getGenre();
                }
            }
        } else {
            Deque<Integer> artistIds = new ArrayDeque<>();
            for (GenreDTO genre : playlistGeneratorDTO.getGenres()) {
                if (genre.getPercentage() == 0) {
                    continue;
                }
                double genreDurationSecond = getGenreDurationSecond(totalDuration, genre);
                Track firstTrack = trackRepository.findRandomTrackByGenre(genre.getGenre());
                artistIds.add(firstTrack.getArtist().getArtistId());
                tracks.add(firstTrack);

                while (durationSeconds < genreDurationSecond - errorMargin) {
                    durationSeconds = addTrackInLoopRandom(tracks, artistIds, durationSeconds, genre.getGenre());
                }
                generatedPlaylist.setDuration(generatedPlaylist.getDuration() + durationSeconds);
                if (durationSeconds > topGenreDuration) {
                    topGenreDuration = durationSeconds;
                    topGenre = genre.getGenre();
                }
            }
        }
        generatedPlaylist.setTopGenre(genreRepository.findByName(topGenre));
        shuffleTracksIfMoreThanOneGenre(playlistGeneratorDTO, tracks, generatedPlaylist);
        playlistRepository.save(generatedPlaylist);
        logger.info(String.format(PLAYLIST_SAVED_MESSAGE,
                generatedPlaylist.getDuration(), generatedPlaylist.getTracks().size()));
        for (Genre genre : generatedPlaylist.getGenres()) {
            genre.getPlaylists().add(generatedPlaylist);
            genreRepository.save(genre);
        }
        logger.info(PLAYLIST_ADDED_TO_GENRES_MESSAGE);
        return ModelMapper.playlistToDTO(generatedPlaylist);
    }

    private double getGenreDurationSecond(long totalDuration, GenreDTO genre) {
        return totalDuration * genre.getPercentage() / 100.0;
    }

    private long updateDurationSecondsAllowSameArtist(Deque<Track> tracks, Deque<Integer> trackIds, GenreDTO genre, long durationSeconds) {
        Track firstTrack = addFirstTrackAllowSameArtist(tracks, trackIds, genre);
        durationSeconds = updateDuration(durationSeconds, firstTrack);
        return durationSeconds;
    }

    private Track addFirstTrackAllowSameArtist(Deque<Track> tracks, Deque<Integer> trackIds, GenreDTO genre) {
        Track firstTrack = trackRepository.findTopTrackByGenre(genre.getGenre());
        trackIds.add(firstTrack.getTrackId());
        tracks.add(firstTrack);
        return firstTrack;
    }

    private void shuffleTracksIfMoreThanOneGenre(PlaylistGeneratorDTO playlistGeneratorDTO, Deque<Track> playlist, Playlist generatedPlaylist) {
        if (playlistGeneratorDTO.getGenres().size() > 1) {
            generatedPlaylist.setTracks(new HashSet<>(shuffleTracks(playlist)));
        } else {
            generatedPlaylist.setTracks(new HashSet<>(playlist));
        }
    }

    private List<Track> shuffleTracks(Deque<Track> playlist) {
        List<Track> result = new ArrayList<>(playlist);
        Collections.shuffle(result);
        return result;
    }

    private long addTrackInLoopAllowSameArtistAndTopTracks(Deque<Track> playlist, Deque<Integer> firstTrackIds, long firstDurationSeconds, String firstGenre) {
        Track track = trackRepository.findTopTrackByGenreAndTrackNotIn(firstGenre, firstTrackIds);
        firstDurationSeconds = updateDuration(firstDurationSeconds, track);
        firstTrackIds.add(track.getTrackId());
        playlist.add(track);
        return firstDurationSeconds;
    }

    private long addTrackInLoopRandom(Deque<Track> playlist, Deque<Integer> artistIds, long currentDurationSeconds, String firstGenre) {
        Track track = trackRepository.findRandomTrackByGenreAndArtistNotInSet(firstGenre, artistIds);
        currentDurationSeconds = updateDuration(currentDurationSeconds, track);
        artistIds.add(track.getArtist().getArtistId());
        playlist.add(track);
        return currentDurationSeconds;
    }

    private long addTrackInLoopUseTopTracks(Deque<Track> playlist, Deque<Integer> firstArtistIds, long firstDurationSeconds, String firstGenre) {
        Track track = trackRepository.findTopTrackByGenreAndArtistNotIn(firstGenre, firstArtistIds);
        firstDurationSeconds = updateDuration(firstDurationSeconds, track);
        firstArtistIds.add(track.getArtist().getArtistId());
        playlist.add(track);
        return firstDurationSeconds;
    }

    private long addTrackInLoopAllowSameArtist(Deque<Track> playlist, Deque<Integer> trackIds, long currentDurationSeconds, String firstGenre) {
        Track track = trackRepository.findRandomTrackByGenreAndTrackNotInSet(firstGenre, trackIds);
        currentDurationSeconds = updateDuration(currentDurationSeconds, track);
        trackIds.add(track.getTrackId());
        playlist.add(track);
        return currentDurationSeconds;
    }

    private long updateDuration(long currentDurationSeconds, Track track) {
        currentDurationSeconds += track.getDuration();
        return currentDurationSeconds;
    }
}
