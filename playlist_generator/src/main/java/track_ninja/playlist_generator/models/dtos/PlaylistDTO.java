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

    private String title;

    private String username;

    private long duration;

    private List<TrackDTO> tracks;
}
