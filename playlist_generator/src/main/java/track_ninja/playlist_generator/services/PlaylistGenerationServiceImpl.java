package track_ninja.playlist_generator.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import track_ninja.playlist_generator.models.Track;
import track_ninja.playlist_generator.models.dtos.PlaylistGenerationDTO;
import track_ninja.playlist_generator.repositories.PlaylistRepository;
import track_ninja.playlist_generator.repositories.TrackRepository;
import track_ninja.playlist_generator.repositories.UserRepository;

import java.util.*;

@Service
public class PlaylistGenerationServiceImpl implements PlaylistGenerationService {
    private TrackRepository trackRepository;
    private PlaylistRepository playlistRepository;
    private UserRepository userRepository;

    @Autowired
    public PlaylistGenerationServiceImpl(TrackRepository trackRepository, PlaylistRepository playlistRepository, UserRepository userRepository) {
        this.trackRepository = trackRepository;
        this.playlistRepository = playlistRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Iterable<Track> generatePlaylist(long playlistDurationSeconds, PlaylistGenerationDTO playlistGenerationDTO) {
        Deque<Track> playlist = new ArrayDeque<>();
        if (playlistGenerationDTO.isAllowSameArtists()){
            if (playlistGenerationDTO.isUseTopTracks()) {
                if (playlistGenerationDTO.getSecondGenre() != null) {
                    Deque<Long> firstTrackIds = new ArrayDeque<>();
                    long firstDurationSeconds = 0L;
                    double firstGenrePercentage = playlistGenerationDTO.getFirstGenrePercentage() / 100.0;
                    Track firstGenreFirstTrack = trackRepository.findTopTrackByGenre(playlistGenerationDTO.getFirstGenre());
                    firstDurationSeconds = updateDuration(firstDurationSeconds, firstGenreFirstTrack);
                    firstTrackIds.add(firstGenreFirstTrack.getTrackId());
                    playlist.add(firstGenreFirstTrack);

                    while (firstDurationSeconds < playlistDurationSeconds * firstGenrePercentage - 150) {
                        firstDurationSeconds = addTrackInLoopAllowSameArtistAndTracks(playlist, firstTrackIds, firstDurationSeconds, playlistGenerationDTO.getFirstGenre());
                    }

                    Deque<Long> secondTrackIds = new ArrayDeque<>();
                    long secondDurationSeconds = 0L;
                    double secondGenrePercentage = playlistGenerationDTO.getSecondGenrePercentage() / 100.0;
                    Track secondGenreFirstTrack = trackRepository.findRandomTrackByGenre(playlistGenerationDTO.getSecondGenre());
                    secondDurationSeconds = updateDuration(secondDurationSeconds, secondGenreFirstTrack);
                    secondTrackIds.add(secondGenreFirstTrack.getTrackId());
                    playlist.add(secondGenreFirstTrack);

                    while (secondDurationSeconds < playlistDurationSeconds * secondGenrePercentage - 150) {
                        secondDurationSeconds = addTrackInLoopAllowSameArtistAndTracks(playlist, secondTrackIds, secondDurationSeconds, playlistGenerationDTO.getSecondGenre());
                    }
                    return shuffleTracks(playlist);
                }
                Deque<Long> trackIds = new ArrayDeque<>();
                long durationSeconds = 0L;
                Track firstTrack = trackRepository.findTopTrackByGenre(playlistGenerationDTO.getFirstGenre());
                durationSeconds = updateDuration(durationSeconds, firstTrack);
                trackIds.add(firstTrack.getTrackId());
                playlist.add(firstTrack);

                while (durationSeconds < playlistDurationSeconds -300) {
                    durationSeconds = addTrackInLoopAllowSameArtistAndTracks(playlist, trackIds, durationSeconds, playlistGenerationDTO.getFirstGenre());
                }
                return shuffleTracks(playlist);

            }
            if (playlistGenerationDTO.getSecondGenre() != null) {
                Deque<Long> firstTrackIds = new ArrayDeque<>();
                long firstDurationSeconds = 0L;
                double firstGenrePercentage = playlistGenerationDTO.getFirstGenrePercentage() / 100.0;
                Track firstGenreFirstTrack = trackRepository.findRandomTrackByGenre(playlistGenerationDTO.getFirstGenre());
                firstDurationSeconds = updateDuration(firstDurationSeconds, firstGenreFirstTrack);
                firstTrackIds.add(firstGenreFirstTrack.getTrackId());
                playlist.add(firstGenreFirstTrack);

                while (firstDurationSeconds < playlistDurationSeconds * firstGenrePercentage - 150) {
                    firstDurationSeconds = addTrackInLoopAllowSameArtist(playlist, firstTrackIds, firstDurationSeconds, playlistGenerationDTO.getFirstGenre());
                }

                Deque<Long> secondTrackIds = new ArrayDeque<>();
                long secondDurationSeconds = 0L;
                double secondGenrePercentage = playlistGenerationDTO.getSecondGenrePercentage() / 100.0;
                Track secondGenreFirstTrack = trackRepository.findRandomTrackByGenre(playlistGenerationDTO.getSecondGenre());
                secondDurationSeconds = updateDuration(secondDurationSeconds, secondGenreFirstTrack);
                secondTrackIds.add(secondGenreFirstTrack.getTrackId());
                playlist.add(secondGenreFirstTrack);

                while (secondDurationSeconds < playlistDurationSeconds * secondGenrePercentage - 150) {
                    secondDurationSeconds = addTrackInLoopAllowSameArtist(playlist, secondTrackIds, secondDurationSeconds, playlistGenerationDTO.getSecondGenre());
                }
                return shuffleTracks(playlist);
            }
            Deque<Long> trackIds = new ArrayDeque<>();
            long currentDurationSeconds = 0L;
            Track firstTrack = trackRepository.findRandomTrackByGenre(playlistGenerationDTO.getFirstGenre());
            currentDurationSeconds = updateDuration(currentDurationSeconds, firstTrack);
            trackIds.add(firstTrack.getTrackId());
            playlist.add(firstTrack);

            while (currentDurationSeconds < playlistDurationSeconds - 300) {
                currentDurationSeconds = addTrackInLoopAllowSameArtist(playlist, trackIds, currentDurationSeconds, playlistGenerationDTO.getFirstGenre());
            }
            return playlist;
        }
        Deque<Integer> artistIds = new ArrayDeque<>();
        if (playlistGenerationDTO.isUseTopTracks()) {
            if (playlistGenerationDTO.getSecondGenre() != null) {
                Deque<Integer> firstArtistIds = new ArrayDeque<>();
                long firstDurationSeconds = 0L;
                double firstGenrePercentage = playlistGenerationDTO.getFirstGenrePercentage() / 100.0;
                Track firstGenreFirstTrack = trackRepository.findTopTrackByGenre(playlistGenerationDTO.getFirstGenre());
                firstDurationSeconds = updateDuration(firstDurationSeconds, firstGenreFirstTrack);
                firstArtistIds.add(firstGenreFirstTrack.getArtist().getArtistId());
                playlist.add(firstGenreFirstTrack);

                while (firstDurationSeconds < playlistDurationSeconds * firstGenrePercentage - 150) {
                    firstDurationSeconds = addTrackInLoopUseTopTracks(playlist, firstArtistIds, firstDurationSeconds, playlistGenerationDTO.getFirstGenre());
                }

                Deque<Integer> secondArtistIds = new ArrayDeque<>();
                long secondDurationSeconds = 0L;
                double secondGenrePercentage = playlistGenerationDTO.getSecondGenrePercentage() / 100.0;
                Track secondGenreFirstTrack = trackRepository.findTopTrackByGenre(playlistGenerationDTO.getSecondGenre());
                secondDurationSeconds = updateDuration(secondDurationSeconds, secondGenreFirstTrack);
                secondArtistIds.add(secondGenreFirstTrack.getArtist().getArtistId());
                playlist.add(secondGenreFirstTrack);

                while (secondDurationSeconds < playlistDurationSeconds * secondGenrePercentage - 150) {
                    secondDurationSeconds = addTrackInLoopUseTopTracks(playlist, secondArtistIds, secondDurationSeconds, playlistGenerationDTO.getSecondGenre());
                }
                return shuffleTracks(playlist);
            }
            long currentDurationSeconds = 0L;
            Track firstTrack = trackRepository.findTopTrackByGenre(playlistGenerationDTO.getFirstGenre());
            currentDurationSeconds = updateDuration(currentDurationSeconds, firstTrack);
            artistIds.add(firstTrack.getArtist().getArtistId());
            playlist.add(firstTrack);
            while (currentDurationSeconds < playlistDurationSeconds - 300) {
                currentDurationSeconds = addTrackInLoopUseTopTracks(playlist, artistIds, currentDurationSeconds, playlistGenerationDTO.getFirstGenre());
            }
            return playlist;
        }if (playlistGenerationDTO.getSecondGenre() != null) {
            Deque<Integer> firstArtistIds = new ArrayDeque<>();
            long firstDurationSeconds = 0L;
            double firstGenrePercentage = playlistGenerationDTO.getFirstGenrePercentage() / 100.0;
            Track firstGenreFirstTrack = trackRepository.findRandomTrackByGenre(playlistGenerationDTO.getFirstGenre());
            firstDurationSeconds = updateDuration(firstDurationSeconds, firstGenreFirstTrack);
            firstArtistIds.add(firstGenreFirstTrack.getArtist().getArtistId());
            playlist.add(firstGenreFirstTrack);

            while (firstDurationSeconds < playlistDurationSeconds * firstGenrePercentage - 150) {
                firstDurationSeconds = addTrackInLoopRandom(playlist, firstArtistIds, firstDurationSeconds, playlistGenerationDTO.getFirstGenre());
            }

            Deque<Integer> secondArtistIds = new ArrayDeque<>();
            long secondDurationSeconds = 0L;
            double secondGenrePercentage = playlistGenerationDTO.getSecondGenrePercentage() / 100.0;
            Track secondGenreFirstTrack = trackRepository.findRandomTrackByGenre(playlistGenerationDTO.getSecondGenre());
            secondDurationSeconds = updateDuration(secondDurationSeconds, secondGenreFirstTrack);
            secondArtistIds.add(secondGenreFirstTrack.getArtist().getArtistId());
            playlist.add(secondGenreFirstTrack);

            while (secondDurationSeconds < playlistDurationSeconds * secondGenrePercentage - 150) {
                secondDurationSeconds = addTrackInLoopRandom(playlist, secondArtistIds, secondDurationSeconds, playlistGenerationDTO.getSecondGenre());
            }
            return shuffleTracks(playlist);
        }
        long currentDurationSeconds = 0L;
        Track firstTrack = trackRepository.findRandomTrackByGenre(playlistGenerationDTO.getFirstGenre());
        currentDurationSeconds = updateDuration(currentDurationSeconds, firstTrack);
        artistIds.add(firstTrack.getArtist().getArtistId());
        playlist.add(firstTrack);

        while (currentDurationSeconds < playlistDurationSeconds - 300) {
            currentDurationSeconds = addTrackInLoopRandom(playlist, artistIds, currentDurationSeconds, playlistGenerationDTO.getFirstGenre());
        }
        return playlist;
//        Playlist generatedPlaylist = new Playlist();
//        generatedPlaylist.setTitle(title);
//        generatedPlaylist.setUser(userRepository.findByUsername("ivanivanov"));
//        generatedPlaylist.setDeleted(false);
//        generatedPlaylist.setGenres(new ArrayList<>());
//        generatedPlaylist.setTracks(new ArrayList<>(playlist));
//        playlistRepository.save(generatedPlaylist);

    }

    private Iterable<Track> shuffleTracks(Deque<Track> playlist) {
        List<Track> result = new ArrayList<>(playlist);
        Collections.shuffle(result);
        return result;
    }

    private long addTrackInLoopAllowSameArtistAndTracks(Deque<Track> playlist, Deque<Long> firstTrackIds, long firstDurationSeconds, String firstGenre) {
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
