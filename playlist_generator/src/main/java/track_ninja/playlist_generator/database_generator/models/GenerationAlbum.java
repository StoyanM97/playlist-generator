package track_ninja.playlist_generator.database_generator.models;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name="GenerationAlbum")
@Table(name = "albums")
@JsonIgnoreProperties(ignoreUnknown = true)
public class GenerationAlbum {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "album_id")
    private int albumId;

    @Column(name = "title")
    private String title;

    @Column(name="Album_tracklist_url")
    private String tracklist;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "artist_id")
    private GenerationArtist artist;

    @Transient
    long id;

    @Override
    public String toString() {
        return "GenerationAlbum{" +
                "albumId=" + albumId +
                ", title='" + title + '\'' +
                ", tracklist='" + tracklist + '\'' +
                ", generationArtist=" + artist +
                ", id=" + id +
                '}';
    }
}
