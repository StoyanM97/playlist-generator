package track_ninja.playlist_generator.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "playlists")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Playlist {

    private static final int TITLE_MIN_LENGTH = 3;
    private static final int TITLE_MAX_LENGTH = 10;
    private static final String TITLE_LENGTH_ERROR_MESSAGE = "Title length must be at least 3 and at most 20 characters long!";

    private static final String TITLE = "title";
    private static final String PLAYLIST_ID = "playlist_id";
    private static final String USER_ID = "user_id";
    private static final String IS_DELETED = "is_deleted";
    private static final String TRACK_ID = "track_id";
    private static final String PLAYLIST_TRACK_RELATIONS = "playlist_track_relations";
    private static final String PLAYLISTS = "playlists";
    private static final String DURATION = "duration";
    private static final String TOP_GENRE_ID = "top_genre_id";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = PLAYLIST_ID)
    private int playlistId;

    @Size(min = TITLE_MIN_LENGTH, max = TITLE_MAX_LENGTH, message = TITLE_LENGTH_ERROR_MESSAGE)
    @Column(name = TITLE)
    private String title;

    @ManyToOne
    @JoinColumn(name= USER_ID, nullable=false)
    private UserDetails user;

    @Column(name = IS_DELETED)
    private boolean isDeleted;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = PLAYLIST_TRACK_RELATIONS,
            joinColumns = {@JoinColumn(name = PLAYLIST_ID, referencedColumnName = PLAYLIST_ID)},
            inverseJoinColumns = {@JoinColumn(name = TRACK_ID, referencedColumnName = TRACK_ID)})
    private Set<Track> tracks;

    @JsonIgnore
    @ManyToMany(mappedBy = PLAYLISTS)
    private Set<Genre> genres;

    @Column(name = DURATION)
    private Long duration;

    @ManyToOne
    @JoinColumn(name = TOP_GENRE_ID)
    private Genre topGenre;
}
