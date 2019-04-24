package track_ninja.playlist_generator.services;

import track_ninja.playlist_generator.models.dtos.PlaylistDTO;
import track_ninja.playlist_generator.models.dtos.PlaylistGeneratorDTO;


public interface PlaylistGenerationService {
    PlaylistDTO generatePlaylist(PlaylistGeneratorDTO playlistGeneratorDTO);
}
