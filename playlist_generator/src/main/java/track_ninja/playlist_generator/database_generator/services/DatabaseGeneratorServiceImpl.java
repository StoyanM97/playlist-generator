package track_ninja.playlist_generator.database_generator.services;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import track_ninja.playlist_generator.database_generator.common.GenreStyle;
import track_ninja.playlist_generator.database_generator.models.*;
import track_ninja.playlist_generator.database_generator.repositories.*;
import track_ninja.playlist_generator.repositories.GenreRepository;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Service
@Transactional
public class DatabaseGeneratorServiceImpl implements DatabaseGeneratorService {

    private static final String TRACK_INDEX = "?index=0&limit=100";
    private static final String TRACK_NEXT_INDEX = "&index=25";
    private static final String URL_PLAYLIST = "https://api.deezer.com/search/playlist?q=";
    private static final String URL_GENRE = "https://api.deezer.com/genre";

    private static final String GENRES_WERE_LOADED = "Genres were loaded";
    private static final String UNABLE_TO_LOAD_GENRES = "Unable to load Genres";
    private static final String TRACKS_SAVING_FINISHED = "Saving tracts to database finished";
    private static final String PLAYLISTS_FROM_DEEZER_API = "Downloaded first 25 Playlists from Deezer Api for genre ";
    private static final String PLAYLISTS_FROM_DEEZER_API_NEXT_25 = "Downloaded next 25 Playlists from Deezer Api for genre ";
    private static final String NO_TRACKS_DOWNLOADED = "No Tracks downloaded";
    private static final String TRACKS_WERE_DOWNLOADED = "Tracks were downloaded for genre ";
    private static final String THREAD_SLEEP_INTERRUPTED_ERROR = "Thread sleep interrupted error ";
    private static final String NUMBER_OF_TRACKS_DOWNLOADED_FOR_GENRE = "Number of tracks downloaded for genre ";
    private static final String TOTAL_PLAYLISTS_FOR_GENRE = "Total playlists for genre ";
    private static final String DELIMITER = ": ";
    private static final String TOTAL_TRACKS = "Total tracks after removing the duplicate tracks: ";
    private static final String NO_NEW_GENRES_ADDED = "No new genres added";

    private GenreGenerationRepository genreGenerationRepository;
    private TrackGenerationRepository trackGenerationRepository;
    private ArtistGenerationRepository artistGenerationRepository;
    private AlbumGenerationRepository albumGenerationRepository;
    private RestTemplate restTemplate;

    private static final Logger log = LoggerFactory.getLogger(DatabaseGeneratorService.class);

    @Autowired
    public DatabaseGeneratorServiceImpl(GenreGenerationRepository genreGenerationRepository, TrackGenerationRepository trackGenerationRepository,
                                        ArtistGenerationRepository artistGenerationRepository, AlbumGenerationRepository albumGenerationRepository,
                                        RestTemplate restTemplate) {
        this.genreGenerationRepository = genreGenerationRepository;
        this.trackGenerationRepository = trackGenerationRepository;
        this.artistGenerationRepository = artistGenerationRepository;
        this.albumGenerationRepository = albumGenerationRepository;
        this.restTemplate = restTemplate;

    }

    @Override
    public boolean synchronizeGenres() {

        GenreList result = restTemplate.getForObject(URL_GENRE, GenreList.class);

        if(result!=null){
            List<GenerationGenre> generationGenres = result.getGenres();
            List<GenerationGenre> genresToSave = new ArrayList<>();

            generationGenres.forEach(generationGenre -> {
                if(!genreGenerationRepository.existsByName(generationGenre.getName())){
                    genresToSave.add(generationGenre);
                }
            });
            if (genresToSave.isEmpty()) {
                log.info(NO_NEW_GENRES_ADDED);
                return false;
            }
            genreGenerationRepository.saveAll(genresToSave);

            log.info(GENRES_WERE_LOADED);
            return true;
        }
        else{
            log.error(UNABLE_TO_LOAD_GENRES);
            return false;
        }

    }

    @Override
    public boolean tracksAreDownloaded() {
        return trackGenerationRepository.existsByTrackId(1);
    }

    @Override
    public boolean genresAreDownloaded() {
        return genreGenerationRepository.existsByGenreId(1);
    }

    @Override
    public boolean saveGenres() {

        GenreList result = restTemplate.getForObject(URL_GENRE, GenreList.class);

        if(result!=null){
            List<GenerationGenre> generationGenres = result.getGenres();

            genreGenerationRepository.saveAll(generationGenres);

            log.info(GENRES_WERE_LOADED);
            return true;
        }
        else{
            log.error(UNABLE_TO_LOAD_GENRES);
            return false;
        }

    }

    @Override
    public boolean saveTracks() {
        
        Set<GenerationTrack> tracks = getTracks();

        if(tracks.isEmpty()){
            return false;
        }

        tracks.forEach(track -> {

            GenerationGenre generationGenre = track.getGenre();
            if(genreGenerationRepository.existsByName(generationGenre.getName())){
                generationGenre = genreGenerationRepository.getByName(generationGenre.getName());
                track.setGenre(generationGenre);
            }

            GenerationAlbum GenerationAlbum = track.getAlbum();
            if(albumGenerationRepository.existsByTitleAndTracklist(GenerationAlbum.getTitle(), GenerationAlbum.getTracklist())){
                GenerationAlbum = albumGenerationRepository.getByTitleAndTracklist(GenerationAlbum.getTitle(), GenerationAlbum.getTracklist());
                track.setAlbum(GenerationAlbum);

            }

            GenerationArtist generationArtist = track.getArtist();
            if(artistGenerationRepository.existsByNameAndTracklist(generationArtist.getName(), generationArtist.getTracklist())){
                generationArtist = artistGenerationRepository.getByNameAndTracklist(generationArtist.getName(), generationArtist.getTracklist());
                track.setArtist(generationArtist);
                track.getAlbum().setArtist(generationArtist);
            }

            trackGenerationRepository.save(track);

        });

        log.info(TRACKS_SAVING_FINISHED);

        return true;
    }


    private Set<GenerationTrack> getTracks() {

        Set<GenerationTrack> generationTracks = new HashSet<>();

        for (GenreStyle style: GenreStyle.values()) {

            List<GenerationPlaylist> generationPlaylists = getPlaylists(restTemplate, style.getStyle());
            log.info(TOTAL_PLAYLISTS_FOR_GENRE + style.getStyle()+ DELIMITER + generationPlaylists.size());

            List<GenerationTrack> generationTrackList = getTracklist(restTemplate, generationPlaylists, style.getStyle());
            log.info(NUMBER_OF_TRACKS_DOWNLOADED_FOR_GENRE + style.getStyle()+ DELIMITER + generationTrackList.size());

            removeDuplicateTracks(generationTracks, generationTrackList, style.getStyle());

            try{
                Thread.sleep(5000);
            }
            catch (InterruptedException e){

                log.error(THREAD_SLEEP_INTERRUPTED_ERROR + e.getMessage());

            }

        }

        log.info(TOTAL_TRACKS + generationTracks.size());

        return generationTracks;

    }


    private List<GenerationPlaylist> getPlaylists(RestTemplate restTemplate, String style){

        List<GenerationPlaylist> generationPlaylists = new ArrayList<>();

        PlaylistList result = restTemplate.getForObject(URL_PLAYLIST + style, PlaylistList.class);
        if(result!=null){
            generationPlaylists.addAll(result.getPlaylists());
            log.info(PLAYLISTS_FROM_DEEZER_API + style);
        }
        PlaylistList resultNext = restTemplate.getForObject(URL_PLAYLIST +style + TRACK_NEXT_INDEX, PlaylistList.class);
        if(resultNext!=null){
            generationPlaylists.addAll(resultNext.getPlaylists());
            log.info(PLAYLISTS_FROM_DEEZER_API_NEXT_25 + style);
        }
        return generationPlaylists;
    }

    private List<GenerationTrack> getTracklist(RestTemplate restTemplate, List<GenerationPlaylist> generationPlaylists, String style ){

        List<GenerationTrack> generationTrackList = new ArrayList<>();
        for (GenerationPlaylist p: generationPlaylists) {
            TrackList trackListResult = restTemplate.getForObject(p.getTracklist()+ TRACK_INDEX, TrackList.class);
            if(trackListResult!=null){
                generationTrackList.addAll(trackListResult.getTracks());

            }
        }
        log.info(generationTrackList.isEmpty()? NO_TRACKS_DOWNLOADED : TRACKS_WERE_DOWNLOADED + style);
        return generationTrackList;
    }

    private void removeDuplicateTracks(Set<GenerationTrack> generationTracks, List<GenerationTrack> generationTrackList, String style){

        generationTrackList.forEach(generationTrack -> {

            generationTrack.getAlbum().setArtist(generationTrack.getArtist());
            GenerationGenre generationGenre = new GenerationGenre();
            generationGenre.setName(style);
            generationTrack.setGenre(generationGenre);
            generationTracks.add(generationTrack);

        });
    }

}
