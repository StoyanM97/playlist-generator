package track_ninja.playlist_generator.models.mappers;

import org.apache.tomcat.util.codec.binary.Base64;
import track_ninja.playlist_generator.models.Genre;
import track_ninja.playlist_generator.models.Playlist;
import track_ninja.playlist_generator.models.Track;
import track_ninja.playlist_generator.models.User;
import track_ninja.playlist_generator.models.dtos.PlaylistDTO;
import track_ninja.playlist_generator.models.dtos.TrackDTO;
import track_ninja.playlist_generator.models.dtos.UserDisplayDTO;

import java.util.Collections;
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
        //TODO: set url based on most used genre check if playlist is created throws null point exeption
        playlistDTO.setImageUrl(playlist.getTopGenre().getImageUrl());
        playlistDTO.setUsername(playlist.getUser().getUser().getUsername());
        playlistDTO.setDuration(playlist.getDuration());
        playlistDTO.setAverageRank(playlist.getTracks().stream()
                .mapToInt(Track::getRank)
                .average()
                .orElseThrow(NullPointerException::new));
        playlistDTO.setGenres(playlist.getGenres().stream()
                .map(Genre::getName)
                .collect(Collectors.toList()));
        playlistDTO.setTracks(playlist.getTracks().stream()
                .map(ModelMapper::trackToDTO)
                .collect(Collectors.toList()));
        return playlistDTO;
    }

    public static UserDisplayDTO userToDTO(User user) {
        UserDisplayDTO userDisplayDTO = new UserDisplayDTO();
        userDisplayDTO.setUsername(user.getUsername());
        userDisplayDTO.setRole(user.getAuthority().getName().toString());
        userDisplayDTO.setFirstName(user.getUserDetail().getFirstName());
        userDisplayDTO.setLastName(user.getUserDetail().getLastName());
        userDisplayDTO.setEmail(user.getUserDetail().getEmail());
        if(user.getUserDetail().getAvatar() != null){
            userDisplayDTO.setAvatar(new String(Base64.encodeBase64(user.getUserDetail().getAvatar())));
        }
        return userDisplayDTO;
    }
}
