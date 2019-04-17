package track_ninja.playlist_generator.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import track_ninja.playlist_generator.models.Track;
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
    public Deque<Track> generatePlaylist(String genre, Long playlistDurationSeconds, String title) {
        Deque<Track> playlist = new ArrayDeque<>();
        Deque<Integer> artistIds = new ArrayDeque<>();
        long currentDurationSeconds = 0L;
        Track firstTrack = trackRepository.findRandomTrackByGenre(genre);
        currentDurationSeconds = updateDuration(currentDurationSeconds, firstTrack);
        updateDeques(playlist, artistIds, firstTrack);

        while (currentDurationSeconds < playlistDurationSeconds - 300) {
            Track track = trackRepository.findRandomTrackByGenreAndArtistNotInSet(genre, artistIds);
            currentDurationSeconds = updateDuration(currentDurationSeconds, track);
            updateDeques(playlist, artistIds, track);
        }

//        Playlist generatedPlaylist = new Playlist();
//        generatedPlaylist.setTitle(title);
//        generatedPlaylist.setUser(userRepository.findByUsername("ivanivanov"));
//        generatedPlaylist.setDeleted(false);
//        generatedPlaylist.setGenres(new ArrayList<>());
//        generatedPlaylist.setTracks(new ArrayList<>(playlist));
//        playlistRepository.save(generatedPlaylist);
        return playlist;
    }

    private void updateDeques(Deque<Track> playlist, Deque<Integer> artistIds, Track firstTrack) {
        artistIds.add(firstTrack.getArtist().getArtistId());
        playlist.add(firstTrack);
    }

    private long updateDuration(long currentDurationSeconds, Track track) {
        currentDurationSeconds += track.getDuration();
        return currentDurationSeconds;
    }
}
