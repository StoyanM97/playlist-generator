package track_ninja.playlist_generator.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TrackDTO {

    private int trackId;

    private String title;

    private String previewUrl;

    private String duration;

    private int rank;

    private String link;

    private String albumName;

    private String artistName;

    private String genreName;

}
