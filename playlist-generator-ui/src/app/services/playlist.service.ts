import { Injectable } from '@angular/core';
import { HttpHeaders, HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Playlist } from '../models/playlist';

@Injectable({ providedIn: 'root' })
export class PlaylistService{

    playlists: Playlist[];
    
    private readonly HOST = 'http://localhost:8080';
    private readonly GET_PLAYLISTS = '';
    
    httpOptions = {
        headers: new HttpHeaders({'Content-Type': 'application/json'})
    };

    constructor(private httpClient: HttpClient){
    this.playlists = [];
    for(var i=1; i<=10; i++){
      var p: Playlist = new Playlist();
      p.title = "Track " +  i;
      p.duration = 123;
      this.playlists.push(p);
    }
    
    //this.playlists.forEach(p=>console.log(p.title));
    }
    
    getPlaylists(): Observable<Playlist[]> {
      return this.httpClient.get<Playlist[]>(this.GET_PLAYLISTS);
    }
    
    getPlaylistsTest():Playlist[] {
        return this.playlists;
    }


}