package track_ninja.database_generator.models;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Transient;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Track {


    @Transient
    int id;
    String title;
    String link;
    int duration;
    int rank;
    String preview;
    Genre genre;
    Artist artist;
    Album album;

}
