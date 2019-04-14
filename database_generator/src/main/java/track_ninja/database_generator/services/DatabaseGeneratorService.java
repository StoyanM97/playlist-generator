package track_ninja.database_generator.services;

import org.springframework.web.client.RestTemplate;

public interface DatabaseGeneratorService {

    void saveGenres(RestTemplate restTemplate);
    void getTracks(RestTemplate restTemplate);
}
