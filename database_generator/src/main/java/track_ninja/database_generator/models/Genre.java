package track_ninja.database_generator.models;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;


import javax.persistence.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "Genre")
@Table(name = "genres")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Genre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "genre_id")
    private int genreId;

    @Column(name = "name")
    private String name;

    @Column(name = "image_url")
    private String picture_big;

    @Transient
    long id;

    @Override
    public String toString() {
        return "Genre{" +
                "genreId=" + genreId +
                ", name='" + name + '\'' +
                ", picture_big='" + picture_big + '\'' +
                ", id=" + id +
                '}';
    }
}
