package track_ninja.database_generator.models;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "Track")
@Table(name = "tracks")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Track {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "track_id")
    int trackId;

    @Column(name = "title")
    String title;

    @Column(name="preview_url")
    String preview;

    @Column(name="duration")
    int duration;

    @Column(name="[rank]")
    int rank;

    @Column(name="link")
    String link;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "genre_id")
    Genre genre;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "artist_id")
    Artist artist;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "album_id")
    Album album;

    @Transient
    long id;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Track track = (Track) o;
        return getId() == track.getId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }


    @Override
    public String toString() {
        return "Track{" +
                "trackId=" + trackId +
                ", title='" + title + '\'' +
                ", preview='" + preview + '\'' +
                ", duration=" + duration +
                ", rank=" + rank +
                ", link='" + link + '\'' +
                ", genre=" + genre +
                ", artist=" + artist +
                ", album=" + album +
                ", id=" + id +
                '}';
    }
}
