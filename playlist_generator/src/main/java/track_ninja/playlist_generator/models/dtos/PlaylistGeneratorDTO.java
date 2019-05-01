package track_ninja.playlist_generator.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PlaylistGeneratorDTO {

    private String title;

    private String travelFrom;

    private String travelTo;

    private List<GenreDTO> genres;

    private boolean allowSameArtists;

    private boolean useTopTracks;

    private String username;

    @Override
    public String toString() {
        return "title: " + title +
                "; travelFrom: " + travelFrom +
                "; travelTo: " + travelTo +
                "; genres: " + genresToString() +
                "; allowSameArtists: " + allowSameArtists +
                "; useTopTracks: " + useTopTracks +
                "; user: " + username;
    }

    private String genresToString() {
        StringBuilder builder = new StringBuilder("(");
        for (GenreDTO genre : genres) {
            builder.append(genre.getGenre()).append("-").append(genre.getPercentage()).append("%, ");
        }
        builder.append(")");
        return builder.toString();
    }
}
