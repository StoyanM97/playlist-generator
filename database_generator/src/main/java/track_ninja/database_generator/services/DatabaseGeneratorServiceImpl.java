package track_ninja.database_generator.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import track_ninja.database_generator.models.Genre;
import track_ninja.database_generator.models.GenreList;
import track_ninja.database_generator.models.Track;
import track_ninja.database_generator.models.TrackList;
import track_ninja.database_generator.repositories.GenreRepository;

import java.util.List;

@Service
public class DatabaseGeneratorServiceImpl implements DatabaseGeneratorService {

    // private static final Logger log = LoggerFactory.getLogger(DatabaseGeneratorApplication.class);

    private GenreRepository repository;

    @Autowired
    public DatabaseGeneratorServiceImpl(GenreRepository repository) {
        this.repository = repository;
    }

    @Override
    public void saveGenres(RestTemplate restTemplate) {

        GenreList result = restTemplate.getForObject("https://api.deezer.com/genre", GenreList.class);
        List<Genre> genres = result.getGenres();
        System.out.println(genres);
        repository.saveAll(genres);
    }

    @Override
    public void saveTracks(RestTemplate restTemplate) {

        TrackList result = restTemplate.getForObject("https://api.deezer.com/playlist/1677006641/tracks", TrackList.class);
        List<Track> tracks = result.getTracks();

        System.out.println(tracks.get(0).getTitle());
        System.out.println(tracks.get(0).getId());
        System.out.println(tracks.get(0).getArtist().getName());
        System.out.println(tracks.get(0).getAlbum().getTitle());
        System.out.println(tracks.size());

    }
}
