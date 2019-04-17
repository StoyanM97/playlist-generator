package track_ninja.location_generator.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import track_ninja.location_generator.services.LocationService;


@CrossOrigin(origins = {"http://localhost:8080","http://localhost:4200" })
@RestController
@RequestMapping("/distance/minutes")
public class LocationServiceController {

    private LocationService service;

    @Autowired
    public LocationServiceController(LocationService service) {
        this.service = service;
    }


    @GetMapping("/location")
    public boolean getLocation(){
        return service.getLocation();
    }
}
