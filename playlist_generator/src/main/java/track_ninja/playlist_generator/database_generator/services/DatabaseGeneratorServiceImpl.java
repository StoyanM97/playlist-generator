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
    private static final String PLAYLISTS_FROM_DEEZER_API = "Downloaded first 25 Playlists from Deezer Api for Genre ";
    private static final String PLAYLISTS_FROM_DEEZER_API_NEXT_25 = "Downloaded next 25 Playlists from Deezer Api for Genre ";
    private static final String NO_TRACKS_DOWNLOADED = "No Tracks downloaded";
    private static final String TRACKS_WERE_DOWNLOADED = "Tracks were downloaded for Genre ";
    private static final String THREAD_SLEEP_INTERRUPTED_ERROR = "Thread sleep interrupted error ";
    private static final String NUMBER_OF_TRACKS_DOWNLOADED_FOR_GENRE = "Number of tracks downloaded for genre ";
    private static final String TOTAL_PLAYLISTS_FOR_GENRE = "Total playlists for genre ";
    private static final String DELIMITER = ": ";
    private static final String TOTAL_TRACKS = "Total tracks after removing the duplicate tracks: ";

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
            List<Genre> genres = result.getGenres();
            List<Genre> genresToSave = new ArrayList<>();

            genres.forEach(genre -> {
                if(!genreGenerationRepository.existsByName(genre.getName())){
                    genresToSave.add(genre);
                }
            });

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
            List<Genre> genres = result.getGenres();

            genreGenerationRepository.saveAll(genres);

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
        
        Set<Track> tracks = getTracks();

        if(tracks.isEmpty()){
            return false;
        }

        tracks.forEach(track -> {

            Genre genre = track.getGenre();
            if(genreGenerationRepository.existsByName(genre.getName())){
                genre = genreGenerationRepository.getByName(genre.getName());
                track.setGenre(genre);
            }

            Album album = track.getAlbum();
            if(albumGenerationRepository.existsByTitleAndTracklist(album.getTitle(),album.getTracklist())){
                album = albumGenerationRepository.getByTitleAndTracklist(album.getTitle(),album.getTracklist());
                track.setAlbum(album);

            }

            Artist artist = track.getArtist();
            if(artistGenerationRepository.existsByNameAndTracklist(artist.getName(), artist.getTracklist())){
                artist = artistGenerationRepository.getByNameAndTracklist(artist.getName(), artist.getTracklist());
                track.setArtist(artist);
                track.getAlbum().setArtist(artist);
            }

            trackGenerationRepository.save(track);

        });

        log.info(TRACKS_SAVING_FINISHED);

        return true;
    }


    private Set<Track> getTracks() {

        Set<Track> tracks = new HashSet<>();

        for (GenreStyle style: GenreStyle.values()) {

            List<Playlist> playlists = getPlaylists(restTemplate, style.getStyle());
            log.info(TOTAL_PLAYLISTS_FOR_GENRE + style.getStyle()+ DELIMITER + playlists.size());

            List<Track> trackList = getTracklist(restTemplate, playlists, style.getStyle());
            log.info(NUMBER_OF_TRACKS_DOWNLOADED_FOR_GENRE + style.getStyle()+ DELIMITER + tracks.size());

            removeDuplicateTracks(tracks, trackList, style.getStyle());

            try{
                Thread.sleep(5000);
            }
            catch (InterruptedException e){

                log.error(THREAD_SLEEP_INTERRUPTED_ERROR + e.getMessage());

            }

        }

        log.info(TOTAL_TRACKS + tracks.size());

        return tracks;

    }


    private List<Playlist> getPlaylists(RestTemplate restTemplate, String style){

        List<Playlist> playlists = new ArrayList<>();

        PlaylistList result = restTemplate.getForObject(URL_PLAYLIST + style, PlaylistList.class);
        if(result!=null){
            playlists.addAll(result.getPlaylists());
            log.info(PLAYLISTS_FROM_DEEZER_API + style);
        }
        PlaylistList resultNext = restTemplate.getForObject(URL_PLAYLIST +style + TRACK_NEXT_INDEX, PlaylistList.class);
        if(resultNext!=null){
            playlists.addAll(resultNext.getPlaylists());
            log.info(PLAYLISTS_FROM_DEEZER_API_NEXT_25 + style);
        }
        return playlists;
    }

    private List<Track> getTracklist(RestTemplate restTemplate, List<Playlist> playlists, String style ){

        List<Track> trackList = new ArrayList<>();
        for (Playlist p: playlists) {
            TrackList trackListResult = restTemplate.getForObject(p.getTracklist()+ TRACK_INDEX, TrackList.class);
            if(trackListResult!=null){
                trackList.addAll(trackListResult.getTracks());

            }
        }
        log.info(trackList.isEmpty()? NO_TRACKS_DOWNLOADED : TRACKS_WERE_DOWNLOADED + style);
        return trackList;
    }

    private void removeDuplicateTracks(Set<Track> tracks, List<Track> trackList, String style){

        trackList.forEach(track -> {

            track.getAlbum().setArtist(track.getArtist());
            Genre genre = new Genre();
            genre.setName(style);
            track.setGenre(genre);
            tracks.add(track);

        });
    }

}
