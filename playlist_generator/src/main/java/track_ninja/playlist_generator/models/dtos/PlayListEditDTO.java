package track_ninja.playlist_generator.models.dtos;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Size;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PlayListEditDTO {
    private static final int TITLE_MIN_LENGTH = 3;
    private static final int TITLE_MAX_LENGTH = 20;
    private static final String TITLE_LENGTH_ERROR_MESSAGE = "Title length must be at least 3 and at most 20 characters long!";

    private int playlistId;

    private String username;

    @Size(min = TITLE_MIN_LENGTH, max = TITLE_MAX_LENGTH, message = TITLE_LENGTH_ERROR_MESSAGE)
    private String title;
}
