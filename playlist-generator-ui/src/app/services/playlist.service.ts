import { Injectable } from '@angular/core';
import { HttpHeaders, HttpClient } from '@angular/common/http';
import { Observable, BehaviorSubject } from 'rxjs';
import { Playlist } from '../models/playlist';
import { Track } from '../models/track';
import { AuthenticationService } from './authentication.service';
import { PlaylistGenerator } from '../models/playlistGenerator';

@Injectable({ providedIn: 'root' })
export class PlaylistService{

    playlists: Playlist[];

    
    playlistExistSubject: BehaviorSubject<boolean>;
    playlistExist: Observable<boolean>;
    
    private readonly HOST = 'http://localhost:8080';
    private readonly PLAYLIST_URL = this.HOST + '/api/playlist';

    private readonly CREATE_PLAYLIST_URL = this.HOST + '/api/user/generate';
    private readonly EXIST_PLAYLIST_URL = this.PLAYLIST_URL + '/exist';
    private readonly DELETE_PLAYLIST_URL = this.HOST + '/api/user/playlist/delete';
    private readonly EDIT_PLAYLIST_URL = this.HOST + '/api/user/playlist/edit';

    private readonly FILTER_PLAYLIST_GENRE_URL = this.PLAYLIST_URL+ "/filter/genre";
    private readonly FILTER_PLAYLIST_TITLE_URL = this.PLAYLIST_URL+ '/filter';
    private readonly FILTER_PLAYLIST_USERNAME_URL = this.PLAYLIST_URL+ '/filter/user';
    private readonly FILTER_PLAYLIST_DURATION_URL = this.PLAYLIST_URL+ '/filter/duration';
   
    httpOptions = {
        headers: new HttpHeaders({'Content-Type': 'application/json'})
    };

    constructor(private httpClient: HttpClient, private authenticationService: AuthenticationService){
      this.playlistExistSubject = new BehaviorSubject<any>(false);
      this.playlistExist = this.playlistExistSubject.asObservable();
    }
    
    public get isPlaylistExist(): boolean {
      return this.playlistExistSubject.value;
    }
  
    setPlaylistExistValue(exist: boolean){
      this.playlistExistSubject.next(exist);
    }
    createPlaylist(playlist: PlaylistGenerator): Observable<Playlist> {
      const body = JSON.stringify(playlist); 
      return this.httpClient.post<Playlist>(this.CREATE_PLAYLIST_URL, body, { headers: this.authenticationService.getHeader()});
    }

    playlistsExistInDB(): Observable<boolean>{
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
    
    editPlaylist(title: string, playlistId: number): Observable<boolean>{
      const playlistToSave = {
        playlistId: playlistId,
        title: title
    };
      const url = `${this.EDIT_PLAYLIST_URL}/${playlistId}`;
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

  getPlaylistsFilterByDuration(duration: number): Observable<Playlist[]> {
    const url = `${this.FILTER_PLAYLIST_DURATION_URL}/${duration}`;
    return this.httpClient.get<Playlist[]>(url,this.httpOptions);
  }


  getPlaylistLocal(playlistId: number): Playlist{
    return this.playlists.filter(playlist => playlist.playlistId === playlistId)[0];
  }

  updatePlaylistLocal(playlistIn: Playlist){
    this.playlists = this.playlists.map(playlist => {
      if(playlist.playlistId === playlistIn.playlistId){
          playlist = Object.assign({}, playlist, playlistIn);
      }
      return playlist; });
  }

  deletePlaylistLocal(playlistId: number){
    this.playlists = this.playlists.filter(playlist => playlist.playlistId !== playlistId);
      
  }
    
}