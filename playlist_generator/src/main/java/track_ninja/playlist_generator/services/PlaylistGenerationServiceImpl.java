package track_ninja.playlist_generator.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import track_ninja.playlist_generator.duration.generator.services.LocationService;
import track_ninja.playlist_generator.models.*;
import track_ninja.playlist_generator.models.dtos.PlaylistDTO;
import track_ninja.playlist_generator.models.dtos.PlaylistGeneratorDTO;
import track_ninja.playlist_generator.exceptions.DurationTooLongException;
import track_ninja.playlist_generator.models.mappers.ModelMapper;
import track_ninja.playlist_generator.repositories.GenreRepository;
import track_ninja.playlist_generator.repositories.PlaylistRepository;
import track_ninja.playlist_generator.repositories.TrackRepository;
import track_ninja.playlist_generator.repositories.UserDetailsRepository;

import java.util.*;

@Service
public class PlaylistGenerationServiceImpl implements PlaylistGenerationService {

    private static final String INITIATED_PLAYLIST_GENERATION_MESSAGE = "Initiated playlist generation. Parameters: %s";
    private static final String PLAYLIST_SAVED_MESSAGE = "playlist successfully saved! Total duration: %d seconds; Total number of tracks: %d";
    private static final String PLAYLIST_ADDED_TO_GENRES_MESSAGE = "playlist successfully added to all of it's genres!";

    private static final Logger logger = LoggerFactory.getLogger(PlaylistGenerationService.class);

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

        long totalDuration = locationService.getTravelDuration(playlistGeneratorDTO.getTravelFrom(), playlistGeneratorDTO.getTravelTo()) * 60;

        if (totalDuration > 580000) {
            DurationTooLongException dtl = new DurationTooLongException();
            logger.error(dtl.getMessage());
            throw dtl;
        }

        Deque<Track> tracks = new ArrayDeque<>();
        Playlist generatedPlaylist = new Playlist();
        generatedPlaylist.setTitle(playlistGeneratorDTO.getTitle());
        generatedPlaylist.setUser(userDetailsRepository.findByIsDeletedFalseAndUser_Username(playlistGeneratorDTO.getUsername()));
        generatedPlaylist.setDeleted(false);
        generatedPlaylist.setDuration(0L);
        List<Genre> genres = new ArrayList<>();
        for (String genreName : playlistGeneratorDTO.getGenres().keySet()) {
            genres.add(genreRepository.findByName(genreName));
        }
        generatedPlaylist.setGenres(genres);

        if (playlistGeneratorDTO.isAllowSameArtists()) {
            if (playlistGeneratorDTO.isUseTopTracks()) {
                Deque<Long> trackIds = new ArrayDeque<>();
                for (String genre : playlistGeneratorDTO.getGenres().keySet()) {
                    if (playlistGeneratorDTO.getGenres().get(genre) == 0) {
                        continue;
                    }
                    double genreDurationSecond = totalDuration * playlistGeneratorDTO.getGenres().get(genre) / 100.0;
                    long durationSeconds = 0L;
                    Track firstTrack = trackRepository.findTopTrackByGenre(genre);
                    durationSeconds = updateDuration(durationSeconds, firstTrack);
                    trackIds.add(firstTrack.getTrackId());
                    tracks.add(firstTrack);

                    while (durationSeconds < genreDurationSecond - 150) {
                        durationSeconds = addTrackInLoopAllowSameArtistAndTopTracks(tracks, trackIds, durationSeconds, genre);
                    }
                    generatedPlaylist.setDuration(generatedPlaylist.getDuration() + durationSeconds);
                }
            } else {
                Deque<Long> trackIds = new ArrayDeque<>();
                for (String genre : playlistGeneratorDTO.getGenres().keySet()) {
                    if (playlistGeneratorDTO.getGenres().get(genre) == 0) {
                        continue;
                    }
                    double genreDurationSecond = totalDuration * playlistGeneratorDTO.getGenres().get(genre) / 100.0;
                    long durationSeconds = 0L;
                    Track firstTrack = trackRepository.findRandomTrackByGenre(genre);
                    durationSeconds = updateDuration(durationSeconds, firstTrack);
                    trackIds.add(firstTrack.getTrackId());
                    tracks.add(firstTrack);

                    while (durationSeconds < genreDurationSecond - 150) {
                        durationSeconds = addTrackInLoopAllowSameArtist(tracks, trackIds, durationSeconds, genre);
                    }
                    generatedPlaylist.setDuration(generatedPlaylist.getDuration() + durationSeconds);
                }
            }
        } else if (playlistGeneratorDTO.isUseTopTracks()) {
            Deque<Integer> artistIds = new ArrayDeque<>();
            for (String genre : playlistGeneratorDTO.getGenres().keySet()) {
                if (playlistGeneratorDTO.getGenres().get(genre) == 0) {
                    continue;
                }
                double genreDurationSecond = totalDuration * playlistGeneratorDTO.getGenres().get(genre) / 100.0;
                long durationSeconds = 0L;
                Track firstTrack = trackRepository.findTopTrackByGenre(genre);
                durationSeconds = updateDuration(durationSeconds, firstTrack);
                artistIds.add(firstTrack.getArtist().getArtistId());
                tracks.add(firstTrack);

                while (durationSeconds < genreDurationSecond - 150) {
                    durationSeconds = addTrackInLoopUseTopTracks(tracks, artistIds, durationSeconds, genre);
                }
                generatedPlaylist.setDuration(generatedPlaylist.getDuration() + durationSeconds);
            }
        } else {
            Deque<Integer> artistIds = new ArrayDeque<>();
            for (String genre : playlistGeneratorDTO.getGenres().keySet()) {
                if (playlistGeneratorDTO.getGenres().get(genre) == 0) {
                    continue;
                }
                double genreDurationSecond = totalDuration * playlistGeneratorDTO.getGenres().get(genre) / 100.0;
                long durationSeconds = 0L;
                Track firstTrack = trackRepository.findRandomTrackByGenre(genre);
                artistIds.add(firstTrack.getArtist().getArtistId());
                tracks.add(firstTrack);

                while (durationSeconds < genreDurationSecond - 150) {
                    durationSeconds = addTrackInLoopRandom(tracks, artistIds, durationSeconds, genre);
                }
                generatedPlaylist.setDuration(generatedPlaylist.getDuration() + durationSeconds);
            }
        }
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

    private void shuffleTracksIfMoreThanOneGenre(PlaylistGeneratorDTO playlistGeneratorDTO, Deque<Track> playlist, Playlist generatedPlaylist) {
        if (playlistGeneratorDTO.getGenres().keySet().size() > 1) {
            generatedPlaylist.setTracks(shuffleTracks(playlist));
        } else {
            generatedPlaylist.setTracks(new ArrayList<>(playlist));
        }
    }

    private List<Track> shuffleTracks(Deque<Track> playlist) {
        List<Track> result = new ArrayList<>(playlist);
        Collections.shuffle(result);
        return result;
    }

    private long addTrackInLoopAllowSameArtistAndTopTracks(Deque<Track> playlist, Deque<Long> firstTrackIds, long firstDurationSeconds, String firstGenre) {
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

    private long addTrackInLoopAllowSameArtist(Deque<Track> playlist, Deque<Long> trackIds, long currentDurationSeconds, String firstGenre) {
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
