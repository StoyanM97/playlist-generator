package track_ninja.playlist_generator.database_generator.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import track_ninja.playlist_generator.database_generator.services.DatabaseGeneratorService;

@RestController
@RequestMapping("/api/admin/download")
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

    @GetMapping("/tracks/exist")
    public boolean tracksAreDownloaded() {
        return service.tracksAreDownloaded();
    }

    @GetMapping("/genres/exist")
    public boolean genresAreDownloaded() {
        return service.genresAreDownloaded();
    }

    @GetMapping("/sync/genres")
    public boolean synchronizeGenres(){
        return service.synchronizeGenres();
    }
}
