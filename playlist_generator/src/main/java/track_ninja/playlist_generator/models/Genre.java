package track_ninja.playlist_generator.models;

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
    private static final String GENRE_ID = "GenreId";
    private static final String NAME = "Name";
    private static final String IMAGE_URL = "ImageUrl";
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
    private Set<Track> tracks;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "genre_playlist_relations",
            joinColumns = {@JoinColumn(name = "GenreId", referencedColumnName = "GenreId")},
            inverseJoinColumns = {@JoinColumn(name = "PlaylistId", referencedColumnName = "PlaylistId")})
    private List<Playlist> playlists;
}
