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
                long estimatedNumberOfTracks = (long)Math.ceil(playlistDurationSeconds / trackRepository.findAverageDuration()) + 1;
                if (playlistGenerationDTO.getSecondGenre() != null) {
                    List<Track> tracks;
                    long estimatedNumberOfFirstGenreTracks = estimatedNumberOfTracks * playlistGenerationDTO.getFirstGenrePercentage() / 100;
                    tracks = trackRepository.findTopGenre(playlistGenerationDTO.getFirstGenre(), estimatedNumberOfFirstGenreTracks);
                    long estimatedNumberOfSecondGenreTracks = estimatedNumberOfTracks * playlistGenerationDTO.getSecondGenrePercentage() / 100;
                    tracks.addAll(trackRepository.findTopGenre(playlistGenerationDTO.getSecondGenre(), estimatedNumberOfSecondGenreTracks));
                    Collections.shuffle(tracks);
                    return tracks;
                }
                return trackRepository.findTopGenre(playlistGenerationDTO.getFirstGenre(), estimatedNumberOfTracks);
            } else if (playlistGenerationDTO.getSecondGenre() != null) {
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
                List<Track> result = new ArrayList<>(playlist);
                Collections.shuffle(result);
                return result;
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
                    Track track = trackRepository.findTopTrackByGenreAndArtistNotIn(playlistGenerationDTO.getFirstGenre(), firstArtistIds);
                    firstDurationSeconds = updateDuration(firstDurationSeconds, track);
                    firstArtistIds.add(track.getArtist().getArtistId());
                    playlist.add(track);
                }

                Deque<Integer> secondArtistIds = new ArrayDeque<>();
                long secondDurationSeconds = 0L;
                double secondGenrePercentage = playlistGenerationDTO.getSecondGenrePercentage() / 100.0;
                Track secondGenreFirstTrack = trackRepository.findTopTrackByGenre(playlistGenerationDTO.getSecondGenre());
                secondDurationSeconds = updateDuration(secondDurationSeconds, secondGenreFirstTrack);
                secondArtistIds.add(secondGenreFirstTrack.getArtist().getArtistId());
                playlist.add(secondGenreFirstTrack);

                while (secondDurationSeconds < playlistDurationSeconds * secondGenrePercentage - 150) {
                    Track track = trackRepository.findTopTrackByGenreAndArtistNotIn(playlistGenerationDTO.getSecondGenre(), secondArtistIds);
                    secondDurationSeconds = updateDuration(secondDurationSeconds, track);
                    secondArtistIds.add(track.getArtist().getArtistId());
                    playlist.add(track);
                }
                List<Track> result = new ArrayList<>(playlist);
                Collections.shuffle(result);
                return result;
            }
            long currentDurationSeconds = 0L;
            Track firstTrack = trackRepository.findTopTrackByGenre(playlistGenerationDTO.getFirstGenre());
            currentDurationSeconds = updateDuration(currentDurationSeconds, firstTrack);
            artistIds.add(firstTrack.getArtist().getArtistId());
            playlist.add(firstTrack);
            while (currentDurationSeconds < playlistDurationSeconds - 300) {
                Track track = trackRepository.findTopTrackByGenreAndArtistNotIn(playlistGenerationDTO.getFirstGenre(), artistIds);
                currentDurationSeconds = updateDuration(currentDurationSeconds, track);
                artistIds.add(track.getArtist().getArtistId());
                playlist.add(track);
            }
            return playlist;
        } else if (playlistGenerationDTO.getSecondGenre() != null) {
            Deque<Integer> firstArtistIds = new ArrayDeque<>();
            long firstDurationSeconds = 0L;
            double firstGenrePercentage = playlistGenerationDTO.getFirstGenrePercentage() / 100.0;
            Track firstGenreFirstTrack = trackRepository.findRandomTrackByGenre(playlistGenerationDTO.getFirstGenre());
            firstDurationSeconds = updateDuration(firstDurationSeconds, firstGenreFirstTrack);
            firstArtistIds.add(firstGenreFirstTrack.getArtist().getArtistId());
            playlist.add(firstGenreFirstTrack);

            while (firstDurationSeconds < playlistDurationSeconds * firstGenrePercentage - 150) {
                Track track = trackRepository.findRandomTrackByGenreAndArtistNotInSet(playlistGenerationDTO.getFirstGenre(), firstArtistIds);
                firstDurationSeconds = updateDuration(firstDurationSeconds, track);
                firstArtistIds.add(track.getArtist().getArtistId());
                playlist.add(track);
            }

            Deque<Integer> secondArtistIds = new ArrayDeque<>();
            long secondDurationSeconds = 0L;
            double secondGenrePercentage = playlistGenerationDTO.getSecondGenrePercentage() / 100.0;
            Track secondGenreFirstTrack = trackRepository.findRandomTrackByGenre(playlistGenerationDTO.getSecondGenre());
            secondDurationSeconds = updateDuration(secondDurationSeconds, secondGenreFirstTrack);
            secondArtistIds.add(secondGenreFirstTrack.getArtist().getArtistId());
            playlist.add(secondGenreFirstTrack);

            while (secondDurationSeconds < playlistDurationSeconds * secondGenrePercentage - 150) {
                Track track = trackRepository.findRandomTrackByGenreAndArtistNotInSet(playlistGenerationDTO.getSecondGenre(), secondArtistIds);
                secondDurationSeconds = updateDuration(secondDurationSeconds, track);
                secondArtistIds.add(track.getArtist().getArtistId());
                playlist.add(track);
            }
            List<Track> result = new ArrayList<>(playlist);
            Collections.shuffle(result);
            return result;
        }
        long currentDurationSeconds = 0L;
        Track firstTrack = trackRepository.findRandomTrackByGenre(playlistGenerationDTO.getFirstGenre());
        currentDurationSeconds = updateDuration(currentDurationSeconds, firstTrack);
        artistIds.add(firstTrack.getArtist().getArtistId());
        playlist.add(firstTrack);

        while (currentDurationSeconds < playlistDurationSeconds - 300) {
            Track track = trackRepository.findRandomTrackByGenreAndArtistNotInSet(playlistGenerationDTO.getFirstGenre(), artistIds);
            currentDurationSeconds = updateDuration(currentDurationSeconds, track);
            artistIds.add(track.getArtist().getArtistId());
            playlist.add(track);
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

    private Iterable<Track> generatePlaylistSimple(long playlistDurationSeconds, PlaylistGenerationDTO playlistGenerationDTO) {
        return null;
    }

    private Iterable<Track> generatePlaylistForAllowSameArtist(long playlistDurationSeconds, PlaylistGenerationDTO playlistGenerationDTO) {
        return null;
    }

    private long addTrackInLoopAllowSameArtist(Deque<Track> playlist, Deque<Long> trackIds, long currentDurationSeconds, String firstGenre) {
        Track track = trackRepository.findRandomTrackByGenreAndTrackNotInSet(firstGenre, trackIds);
        currentDurationSeconds = updateDuration(currentDurationSeconds, track);
        trackIds.add(track.getTrackId());
        playlist.add(track);
        return currentDurationSeconds;
    }

    private Iterable<Track> generatePlaylistForAllowSameArtistAndTwoGenres(long playlistDurationSeconds, PlaylistGenerationDTO playlistGenerationDTO) {
        return null;
    }

    private long updateDuration(long currentDurationSeconds, Track track) {
        currentDurationSeconds += track.getDuration();
        return currentDurationSeconds;
    }
}
