package track_ninja.playlist_generator.tests;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import track_ninja.playlist_generator.models.Track;
import track_ninja.playlist_generator.repositories.TrackRepository;

@Service
public class TestService {
    private TrackRepository trackRepository;

    @Autowired
    public TestService(TrackRepository trackRepository) {
        this.trackRepository = trackRepository;
        randomTrackByGenre = trackRepository.findRandomTrackByGenre("pop");
    }

    public final Track randomTrackByGenre;
}
