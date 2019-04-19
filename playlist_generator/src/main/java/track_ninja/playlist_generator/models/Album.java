package track_ninja.playlist_generator.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "albums")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Album {

    private static final String ALBUM_ID = "album_id";
    private static final String TITLE = "title";
    private static final String ARTIST_ID = "artist_id";
    private static final String ALBUM_TRACK_LIST_URL = "album_tracklist_Url";
    private static final String ALBUM = "album";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = ALBUM_ID)
    private int albumId;

    @Column(name = TITLE)
    private String title;

    @ManyToOne
    @JoinColumn(name = ARTIST_ID)
    private Artist artist;

    @Column(name = ALBUM_TRACK_LIST_URL)
    private String albumTrackListUrl;

    @OneToMany(mappedBy= ALBUM)
    @JsonIgnore
    private Set<Track> tracks;
}
