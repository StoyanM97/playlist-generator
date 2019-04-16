package track_ninja.location_generator.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class LocationServiceImpl {

    private RestTemplate restTemplate;

    @Autowired
    public LocationServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
}
