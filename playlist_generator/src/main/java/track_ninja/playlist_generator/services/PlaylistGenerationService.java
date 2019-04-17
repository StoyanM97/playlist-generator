package track_ninja.playlist_generator.services;

import track_ninja.playlist_generator.models.Track;

import java.util.Deque;
import java.util.List;
import java.util.Set;

public interface PlaylistGenerationService {
    Deque<Track> generatePlaylist(String genre, Long playlistDurationSeconds, String title);
}
