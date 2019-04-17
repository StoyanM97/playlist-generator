package track_ninja.database_generator.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import track_ninja.database_generator.services.DatabaseGeneratorService;

@CrossOrigin(origins = {"http://localhost:8080","http://localhost:4200" })
@RestController
@RequestMapping("/download")
public class DatabaseGeneratorControllerImpl {

    private DatabaseGeneratorService service;

    @Autowired
    public DatabaseGeneratorControllerImpl(DatabaseGeneratorService service) {
        this.service = service;
    }

    @GetMapping("/genres")
    public boolean downloadGenres() {
        return service.saveGenres();
    }


    @GetMapping("/tracks")
    public boolean downloadTracks() {
        return service.saveTracks();
    }
}
