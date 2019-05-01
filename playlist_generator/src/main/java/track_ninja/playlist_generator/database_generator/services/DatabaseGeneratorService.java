package track_ninja.playlist_generator.database_generator.services;

public interface DatabaseGeneratorService {

    boolean saveGenres();
    boolean saveTracks();
    boolean synchronizeGenres();
    boolean tracksAreDownloaded();
    boolean genresAreDownloaded();
}
