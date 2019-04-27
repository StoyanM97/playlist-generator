package track_ninja.playlist_generator.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PlaylistGeneratorDTO {

    private String title;

    private String travelFrom;

    private String travelTo;

    private Map<String, Integer> genres;

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
        for (String genre : genres.keySet()) {
            builder.append(genre).append("-").append(genres.get(genre)).append("%, ");
        }
        builder.append(")");
        return builder.toString();
    }
}
