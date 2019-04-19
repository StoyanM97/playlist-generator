package track_ninja.location_generator.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import track_ninja.location_generator.services.LocationService;


@CrossOrigin(origins = {"http://localhost:8080","http://localhost:4200" })
@RestController
@RequestMapping("/distance")
public class LocationServiceController {

    private LocationService service;

    @Autowired
    public LocationServiceController(LocationService service) {
        this.service = service;
    }


    @GetMapping("/{startPoint}/{endPoint}")
    public long getLocation(@PathVariable String startPoint, @PathVariable String endPoint){
        return service.getTravelDuration(startPoint, endPoint);
    }
}
