package track_ninja.playlist_generator.services;

import track_ninja.playlist_generator.models.Track;
import track_ninja.playlist_generator.models.dtos.PlaylistGenerationDTO;

import java.util.Deque;
import java.util.List;
import java.util.Set;

public interface PlaylistGenerationService {
    Iterable<Track> generatePlaylist(long playlistDurationSeconds, PlaylistGenerationDTO playlistGenerationDTO);
}
