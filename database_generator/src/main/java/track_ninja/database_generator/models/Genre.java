package track_ninja.database_generator.models;


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
@Entity(name = "Genre")
@Table(name = "genres")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Genre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "genre_id")
    long id;

    @Column(name = "name")
    String name;

    @Column(name = "image_url")
    String picture_big;

}
