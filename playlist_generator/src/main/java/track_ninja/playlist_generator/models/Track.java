package track_ninja.playlist_generator.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "tracks")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Track {

    private static final String TRACK_ID = "TrackId";
    private static final String TITLE = "Title";
    private static final String PREVIEW_URL = "PreviewUrl";
    private static final String DURATION = "Duration";
    private static final String RANK = "Rank";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = TRACK_ID)
    private int trackId;

    @Column(name = TITLE)
    private String title;

    @Column(name = PREVIEW_URL)
    private String previewUrl;

    @Column(name = DURATION)
    private int duration;

    @Column(name = RANK)
    private int rank;

    @ManyToOne
    @JoinColumn(name = "AlbumId")
    private Album album;

    @ManyToOne
    @JoinColumn(name = "GenreId")
    private Genre genre;

    @JsonIgnore
    @ManyToMany(mappedBy = "tracks")
    private List<Playlist> playlists;
}
