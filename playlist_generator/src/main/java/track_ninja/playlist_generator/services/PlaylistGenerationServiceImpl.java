package track_ninja.playlist_generator.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import track_ninja.playlist_generator.duration.generator.services.LocationService;
import track_ninja.playlist_generator.models.*;
import track_ninja.playlist_generator.models.dtos.PlaylistDTO;
import track_ninja.playlist_generator.models.dtos.PlaylistGeneratorDTO;
import track_ninja.playlist_generator.repositories.GenreRepository;
import track_ninja.playlist_generator.repositories.PlaylistRepository;
import track_ninja.playlist_generator.repositories.TrackRepository;
import track_ninja.playlist_generator.repositories.UserDetailsRepository;

import java.util.*;

@Service
public class PlaylistGenerationServiceImpl implements PlaylistGenerationService {

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

        long totalDuration = locationService.getTravelDuration(playlistGeneratorDTO.getTravelFrom(), playlistGeneratorDTO.getTravelTo()) * 60;

        Deque<Track> playlist = new ArrayDeque<>();
        Playlist generatedPlaylist = new Playlist();
        generatedPlaylist.setTitle(playlistGeneratorDTO.getTitle());
        generatedPlaylist.setUser(userDetailsRepository.findByUser_Username(playlistGeneratorDTO.getUsername()));
        generatedPlaylist.setDeleted(false);
        generatedPlaylist.setDuration(0L);
        List<Genre> genres = new ArrayList<>();
        for (String genre : playlistGeneratorDTO.getGenres().keySet()) {
            genres.add(genreRepository.findByName(genre));
        }
        generatedPlaylist.setGenres(genres);

        if (playlistGeneratorDTO.isAllowSameArtists()) {
            if (playlistGeneratorDTO.isUseTopTracks()) {
                Deque<Long> trackIds = new ArrayDeque<>();
                for (String genre : playlistGeneratorDTO.getGenres().keySet()) {
                    double genreDurationSecond = totalDuration * playlistGeneratorDTO.getGenres().get(genre) / 100.0;
                    long durationSeconds = 0L;
                    Track firstTrack = trackRepository.findTopTrackByGenre(genre);
                    durationSeconds = updateDuration(durationSeconds, firstTrack);
                    trackIds.add(firstTrack.getTrackId());
                    playlist.add(firstTrack);

                    while (durationSeconds < genreDurationSecond - 150) {
                        durationSeconds = addTrackInLoopAllowSameArtistAndTopTracks(playlist, trackIds, durationSeconds, genre);
                    }
                    generatedPlaylist.setDuration(generatedPlaylist.getDuration() + durationSeconds);
                }
            } else {
                Deque<Long> trackIds = new ArrayDeque<>();
                for (String genre : playlistGeneratorDTO.getGenres().keySet()) {
                    double genreDurationSecond = totalDuration * playlistGeneratorDTO.getGenres().get(genre) / 100.0;
                    long durationSeconds = 0L;
                    Track firstTrack = trackRepository.findRandomTrackByGenre(genre);
                    durationSeconds = updateDuration(durationSeconds, firstTrack);
                    trackIds.add(firstTrack.getTrackId());
                    playlist.add(firstTrack);

                    while (durationSeconds < genreDurationSecond - 150) {
                        durationSeconds = addTrackInLoopAllowSameArtist(playlist, trackIds, durationSeconds, genre);
                    }
                    generatedPlaylist.setDuration(generatedPlaylist.getDuration() + durationSeconds);
                }
            }
        } else if (playlistGeneratorDTO.isUseTopTracks()) {
            Deque<Integer> artistIds = new ArrayDeque<>();
            for (String genre : playlistGeneratorDTO.getGenres().keySet()) {
                double genreDurationSecond = totalDuration * playlistGeneratorDTO.getGenres().get(genre) / 100.0;
                long durationSeconds = 0L;
                Track firstTrack = trackRepository.findTopTrackByGenre(genre);
                durationSeconds = updateDuration(durationSeconds, firstTrack);
                artistIds.add(firstTrack.getArtist().getArtistId());
                playlist.add(firstTrack);

                while (durationSeconds < genreDurationSecond - 150) {
                    durationSeconds = addTrackInLoopUseTopTracks(playlist, artistIds, durationSeconds, genre);
                }
                generatedPlaylist.setDuration(generatedPlaylist.getDuration() + durationSeconds);
            }
        } else {
            Deque<Integer> artistIds = new ArrayDeque<>();
            for (String genre : playlistGeneratorDTO.getGenres().keySet()) {
                double genreDurationSecond = totalDuration * playlistGeneratorDTO.getGenres().get(genre) / 100.0;
                long durationSeconds = 0L;
                Track firstTrack = trackRepository.findRandomTrackByGenre(genre);
                artistIds.add(firstTrack.getArtist().getArtistId());
                playlist.add(firstTrack);

                while (durationSeconds < genreDurationSecond - 150) {
                    durationSeconds = addTrackInLoopRandom(playlist, artistIds, durationSeconds, genre);
                }
                generatedPlaylist.setDuration(generatedPlaylist.getDuration() + durationSeconds);
            }
        }
        shuffleTracksIfMoreThanOneGenre(playlistGeneratorDTO, playlist, generatedPlaylist);
        playlistRepository.save(generatedPlaylist);
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
