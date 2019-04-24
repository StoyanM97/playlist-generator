package track_ninja.playlist_generator.models;

import track_ninja.playlist_generator.models.dtos.PlaylistDTO;
import track_ninja.playlist_generator.models.dtos.TrackDTO;

import java.util.stream.Collectors;

public class ModelMapper {
    public static TrackDTO trackToDTO(Track track) {
        TrackDTO trackDTO = new TrackDTO();
        trackDTO.setTrackId(track.getTrackId());
        trackDTO.setTitle(track.getTitle());
        trackDTO.setPreviewUrl(track.getPreviewUrl());
        trackDTO.setDuration(track.getDuration());
        trackDTO.setRank(track.getRank());
        trackDTO.setLink(track.getLink());
        trackDTO.setAlbumName(track.getAlbum().getTitle());
        trackDTO.setArtistName(track.getArtist().getName());
        trackDTO.setGenreName(track.getGenre().getName());
        return trackDTO;
    }

    public static PlaylistDTO playlistToDTO(Playlist playlist) {
        PlaylistDTO playlistDTO = new PlaylistDTO();
        playlistDTO.setPlaylistId(playlist.getPlaylistId());
        playlistDTO.setTitle(playlist.getTitle());
        playlistDTO.setUsername(playlist.getUser().getUser().getUsername());
        playlistDTO.setDuration(playlist.getDuration());
        playlistDTO.setGenres(playlist.getGenres().stream()
                .map(Genre::getName)
                .collect(Collectors.toList()));
        playlistDTO.setTracks(playlist.getTracks().stream()
                .map(ModelMapper::trackToDTO)
                .collect(Collectors.toList()));
        return playlistDTO;
    }
}
