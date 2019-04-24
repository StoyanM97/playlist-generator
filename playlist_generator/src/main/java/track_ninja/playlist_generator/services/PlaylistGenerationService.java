package track_ninja.playlist_generator.services;

import track_ninja.playlist_generator.models.dtos.PlaylistDTO;
import track_ninja.playlist_generator.models.dtos.PlaylistGenerationDTO;


public interface PlaylistGenerationService {
    PlaylistDTO generatePlaylist(PlaylistGenerationDTO playlistGenerationDTO);
}
