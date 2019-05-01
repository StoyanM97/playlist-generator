import { Injectable } from '@angular/core';
import { HttpHeaders, HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Playlist } from '../models/playlist';
import { Track } from '../models/track';

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
      p.playlistId = i;
      p.title = "Track " +  i;
      p.username = "Username " +  i;
      p.duration = 123;
      p.averageRank = 6;
      p.imageUrl ="https://e-cdns-images.dzcdn.net/images/misc/db7a604d9e7634a67d45cfc86b48370a/1000x1000-000000-80-0-0.jpg";
      p.genres = ["Pop","dance"];
      p.tracks =  this.getTrack();
     
      this.playlists.push(p);
    }
    
    //this.playlists.forEach(p=>console.log(p.title));
    }
    
    getPlaylists(): Observable<Playlist[]> {
      return this.httpClient.get<Playlist[]>(this.GET_PLAYLISTS);
    }
    
    //endpoint to the base
    getPlaylist(playlistId: number): Playlist{
         return this.playlists.filter(playlist => playlist.playlistId === playlistId)[0];
    }
    
    getPlaylistsTest():Playlist[] {
        return this.playlists;
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