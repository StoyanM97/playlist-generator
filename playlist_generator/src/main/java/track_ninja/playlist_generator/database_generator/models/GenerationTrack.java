package track_ninja.playlist_generator.database_generator.models;


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
@Entity(name="GenerationTrack")
@Table(name = "tracks")
@JsonIgnoreProperties(ignoreUnknown = true)
public class GenerationTrack {

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
    long rank;

    @Column(name="link")
    String link;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "genre_id")
    GenerationGenre genre;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "artist_id")
    GenerationArtist artist;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "album_id")
    GenerationAlbum album;

    @Transient
    long id;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GenerationTrack generationTrack = (GenerationTrack) o;
        return getId() == generationTrack.getId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }


    @Override
    public String toString() {
        return "GenerationTrack{" +
                "trackId=" + trackId +
                ", title='" + title + '\'' +
                ", preview='" + preview + '\'' +
                ", duration=" + duration +
                ", rank=" + rank +
                ", link='" + link + '\'' +
                ", generationGenre=" + genre +
                ", generationArtist=" + artist +
                ", GenerationAlbum=" + album +
                ", id=" + id +
                '}';
    }
}
