import { Injectable } from '@angular/core';
import { HttpHeaders, HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Playlist } from '../models/playlist';
import { Track } from '../models/track';
import { AuthenticationService } from './authentication.service';
import { PlaylistGenerator } from '../models/playlistGenerator';

@Injectable({ providedIn: 'root' })
export class PlaylistService{

    playlists: Playlist[];
    
    private readonly HOST = 'http://localhost:8080';
    private readonly PLAYLIST_URL = this.HOST + '/api/playlist';

    private readonly CREATE_PLAYLIST_URL = this.HOST + '/api/user/generate';
    private readonly EXIST_PLAYLIST_URL = this.HOST + '/exist';
    private readonly DELETE_PLAYLIST_URL = this.HOST + '/api/user/playlist/delete';
    private readonly EDIT_PLAYLIST_URL = this.HOST + '/api/user/playlist/edit';

    private readonly FILTER_PLAYLIST_GENRE_URL = this.PLAYLIST_URL+ "/filter/genre";
    private readonly FILTER_PLAYLIST_TITLE_URL = this.PLAYLIST_URL+ '/filter';
    private readonly FILTER_PLAYLIST_USERNAME_URL = this.PLAYLIST_URL+ '/filter/user';
    private readonly FILTER_PLAYLIST_DURATION_URL = this.PLAYLIST_URL+ '/filter/duration';
   
    httpOptions = {
        headers: new HttpHeaders({'Content-Type': 'application/json'})
    };

    constructor(private httpClient: HttpClient, private authenticationService: AuthenticationService){}
    

    createPlaylist(playlist: PlaylistGenerator): Observable<Playlist> {
      const body = JSON.stringify(playlist); 
      return this.httpClient.post<Playlist>(this.CREATE_PLAYLIST_URL, body, { headers: this.authenticationService.getHeader()});
    }

    playlistsExist(): Observable<boolean>{
      return this.httpClient.get<boolean>(this.EXIST_PLAYLIST_URL,this.httpOptions);
    }

    getPlaylists(): Observable<Playlist[]> {
      return this.httpClient.get<Playlist[]>(this.PLAYLIST_URL,this.httpOptions);
    }

    getPlaylist(playlistId: number): Observable<Playlist>{
         const url = `${this.PLAYLIST_URL}/${playlistId}`;
         return this.httpClient.get<Playlist>(url, this.httpOptions);
    }

    deletePlaylist(playlistId: number): Observable<boolean>{
      const url = `${this.DELETE_PLAYLIST_URL}/${playlistId}`;
      return this.httpClient.delete<boolean>(url, { headers: this.authenticationService.getHeader()});
    }
    
    editPlaylist(playlist: Playlist): Observable<boolean>{
      const playlistToSave = {
        playlistId: playlist.playlistId,
        title: playlist.title
    };
      const url = `${this.EDIT_PLAYLIST_URL}/${playlist.playlistId}`;
      return this.httpClient.put<boolean>(url, playlistToSave, { headers: this.authenticationService.getHeader()});
   }

   getPlaylistsFiletrByGenre(genre: string): Observable<Playlist[]> {
    const url = `${this.FILTER_PLAYLIST_GENRE_URL}/${genre}`;
    return this.httpClient.get<Playlist[]>(url,this.httpOptions);
  }

  getPlaylistsFiletByTitile(title: string): Observable<Playlist[]> {
    const url = `${this.FILTER_PLAYLIST_TITLE_URL}/${title}`;
    return this.httpClient.get<Playlist[]>(url,this.httpOptions);
  }

  getPlaylistsFilterByUsername(username: string): Observable<Playlist[]> {
    const url = `${this.FILTER_PLAYLIST_USERNAME_URL}/${username}`;
    return this.httpClient.get<Playlist[]>(url,this.httpOptions);
  }

  getPlaylistsFilterByDuration(minDuration: number, maxDuration: number): Observable<Playlist[]> {
    const url = `${this.FILTER_PLAYLIST_DURATION_URL}/${minDuration}/${maxDuration}`;
    return this.httpClient.get<Playlist[]>(url,this.httpOptions);
  }


  getPlaylistLocalStorege(playlistId: number): Playlist{
    return this.playlists.filter(playlist => playlist.playlistId === playlistId)[0];
  }
    

  getPlaylistsTest():Playlist[] {
       var play = [];
        for(var i=1; i<=10; i++){
          var p: Playlist = new Playlist();
          p.playlistId = i;
          p.title = "Track " +  i;
          p.username = "Username " +  i;
          p.duration = 123;
          p.averageRank = 6;
          p.imageUrl ="https://e-cdns-images.dzcdn.net/images/misc/db7a604d9e7634a67d45cfc86b48370a/1000x1000-000000-80-0-0.jpg";
          p.genres = ["Pop","dance"];
          p.tracks =  this.getTrack();
         
          play.push(p);
        }
    return play;
  }

  getTrack(): Track[]{
    var tracks: Track[];
    tracks = [];
    for(var i=1; i<=200; i++){
      var t: Track = new Track();
      t.trackId = i;
      t.title = "test track";
      t.previewUrl ="https://cdns-preview-7.dzcdn.net/stream/c-77b4be015b3f63e1266f31310d337baa-3.mp3";
      t.duration = 124;
      t.rank = i;
      t.link = "https://www.deezer.com/en/track/510591562";
      t.albumName = "first"
      t.artistName = "no artist";
      t.genreName = "pop";
    
      tracks.push(t);
  }
    return tracks;
  }


}