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
@Entity
@Table(name = "albums")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Album {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "AlbumId")
    int id;

    @Column(name = "Title")
    String title;

    @Column(name="AlbumTracklistUrl")
    String tracklist;

    @ManyToOne
    @JoinColumn(name = "ArtistId")
    Artist artist;


}
