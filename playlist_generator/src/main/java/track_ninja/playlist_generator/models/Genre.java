package track_ninja.playlist_generator.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "genres")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Genre {
    private static final String GENRE_ID = "genre_id";
    private static final String NAME = "name";
    private static final String IMAGE_URL = "image_url";
    private static final String GENRE = "genre";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = GENRE_ID)
    private int genreId;

    @Column(name = NAME)
    private String name;

    @Column(name = IMAGE_URL)
    private String imageUrl;

    @OneToMany(mappedBy= GENRE)
    @JsonIgnore
    private Set<Track> tracks;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "genre_playlist_relations",
            joinColumns = {@JoinColumn(name = "genre_id", referencedColumnName = "genre_id")},
            inverseJoinColumns = {@JoinColumn(name = "playlist_id", referencedColumnName = "playlist_id")})
    private List<Playlist> playlists;
}
