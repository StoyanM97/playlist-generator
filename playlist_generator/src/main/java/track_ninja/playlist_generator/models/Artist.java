package track_ninja.playlist_generator.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "artists")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Artist {

    private static final String ARTIST_ID = "artist_id";
    private static final String ARTIST_NAME = "name";
    private static final String TRACK_LIST_URL = "artist_tracklist_url";
    private static final String ARTIST = "artist";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = ARTIST_ID)
    private int artistId;

    @Column(name = ARTIST_NAME)
    private String name;

    @Column(name = TRACK_LIST_URL)
    private String trackListUrl;

    @JsonIgnore
    @OneToMany(mappedBy = ARTIST)
    private Set<Album> albums;
}
