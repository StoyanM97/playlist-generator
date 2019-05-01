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
@Entity(name="GenerationGenre")
@Table(name = "genres")
@JsonIgnoreProperties(ignoreUnknown = true)
public class GenerationGenre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "genre_id")
    private int genreId;

    @Column(name = "name")
    private String name;

    @Column(name = "image_url")
    private String picture_xl;

    @Transient
    long id;

    @Override
    public String toString() {
        return "GenerationGenre{" +
                "genreId=" + genreId +
                ", name='" + name + '\'' +
                ", picture_big='" + picture_xl + '\'' +
                ", id=" + id +
                '}';
    }
}
