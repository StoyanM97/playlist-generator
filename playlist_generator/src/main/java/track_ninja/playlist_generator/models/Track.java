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

    private static final String TRACK_ID = "track_id";
    private static final String TITLE = "title";
    private static final String PREVIEW_URL = "preview_url";
    private static final String DURATION = "duration";
    private static final String RANK = "rank";
    private static final String ALBUM_ID = "album_id";
    private static final String GENRE_ID = "genre_id";
    private static final String TRACKS = "tracks";
    private static final String ARTIST_ID = "artist_id";
    private static final String LINK = "link";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = TRACK_ID)
    private Long trackId;

    @Column(name = TITLE)
    private String title;

    @Column(name = PREVIEW_URL)
    private String previewUrl;

    @Column(name = DURATION)
    private int duration;

    @Column(name = RANK)
    private int rank;

    @Column(name = LINK)
    private String link;

    @ManyToOne
    @JoinColumn(name = ALBUM_ID)
    private Album album;

    @ManyToOne
    @JoinColumn(name = ARTIST_ID)
    private Artist artist;

    @ManyToOne
    @JoinColumn(name = GENRE_ID)
    private Genre genre;

    @ManyToMany(mappedBy = TRACKS)
    private List<Playlist> playlists;
}
