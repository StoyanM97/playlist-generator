package track_ninja.playlist_generator.models;

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

    private static final String ALBUM_ID = "AlbumId";
    private static final String TITLE = "Title";
    private static final String ARTIST_ID = "ArtistId";
    private static final String ALBUM_TRACK_LIST_URL = "AlbumTracklistUrl";
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
    private Set<Track> tracks;
}
