package track_ninja.playlist_generator.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PlaylistGenerationDTO {
    private String title;

    private String firstGenre;

    private String secondGenre;

    private int firstGenrePercentage;

    private int secondGenrePercentage;

    private boolean allowSameArtists;

    private boolean useTopTracks;
}
