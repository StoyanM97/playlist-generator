package track_ninja.playlist_generator.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
@Table(name = "playlists")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Playlist {
    private static final String PLAYLIST_ID = "PlaylistId";
    private static final int TITLE_MIN_LENGTH = 3;
    private static final int TITLE_MAX_LENGTH = 20;
    private static final String TITLE_LENGTH_ERROR_MESSAGE = "Title length must be at least 3 and at most 20 characters long!";
    private static final String TITLE = "Title";
    private static final String USER_ID = "UserId";
    private static final String IS_DELETED = "IsDeleted";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = PLAYLIST_ID)
    private int albumId;

    @Size(min = TITLE_MIN_LENGTH, max = TITLE_MAX_LENGTH, message = TITLE_LENGTH_ERROR_MESSAGE)
    @Column(name = TITLE)
    private String title;

    @ManyToOne
    @JoinColumn(name= USER_ID, nullable=false)
    private User user;

    @Column(name = IS_DELETED)
    private boolean isDeleted;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "playlist_track_relations",
            joinColumns = {@JoinColumn(name = "PlaylistId", referencedColumnName = "PlaylistId")},
            inverseJoinColumns = {@JoinColumn(name = "TrackId", referencedColumnName = "TrackId")})
    private List<Track> tracks;

    @JsonIgnore
    @ManyToMany(mappedBy = "playlists")
    private List<Genre> genres;
}
