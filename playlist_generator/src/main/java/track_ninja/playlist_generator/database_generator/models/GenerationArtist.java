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
@Entity(name="GenerationArtist")
@Table(name = "artists")
@JsonIgnoreProperties(ignoreUnknown = true)
public class GenerationArtist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "artist_id")
    private int artistId;

    @Column(name = "name")
    private String name;

    @Column(name = "artist_tracklist_url")
    private String tracklist;

    @Transient
    long id;

    @Override
    public String toString() {
        return "GenerationArtist{" +
                "artistId=" + artistId +
                ", name='" + name + '\'' +
                ", tracklist='" + tracklist + '\'' +
                ", id=" + id +
                '}';
    }
}
