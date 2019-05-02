package track_ninja.playlist_generator.models.dtos;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PlaylistDTO {

    private int playlistId;

    private String title;

    private String username;

    private String duration;

    private double averageRank;

    private String imageUrl;

    private List<String> genres;

    private List<TrackDTO> tracks;
}
