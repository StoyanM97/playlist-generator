import { Component, OnInit } from '@angular/core';
import { Playlist } from '../models/playlist';
import { PlaylistService } from '../services/playlist.service';
import { Router } from '@angular/router';

@Component({
  selector: 'playlists-dashboard',
  templateUrl: './playlists-dashboard.component.html',
  styleUrls: ['./playlists-dashboard.component.scss']
})
export class PlaylistsDashboardComponent implements OnInit {
 
  playlistFullStack: Playlist[];
  playlists: Playlist[];
  
  hasPlaylists: boolean;
  forward: boolean;
  backward: boolean;
  trackList: number;
  nextStack: number;
  
  forwardDisabled: boolean;
  backwarDdisabled: boolean;

  constructor(private playlistService: PlaylistService, private router: Router) {
    this.trackList = 0;
    this.nextStack = 3;
    this.forward = false;
    this.backward = false;

   }

  ngOnInit() {
    this.playlistService.getPlaylists().subscribe(data => {
      console.log(data);
      this.playlistFullStack = data;
  },error => {
      console.log(error);
    },() => { 
      this.playlists = this.playlistFullStack.slice(this.trackList, this.trackList+=this.nextStack);
      this.forwardDisabled = this.validateNext;
      this.backwarDdisabled = true;
    });
   
  }

  showPlaylistDetails(value: Playlist){
    this.router.navigate(['/playlist-details', value.playlistId]);
  }

  next(){
     console.log(this.playlists.length);
     this.forward = true;
     if(this.backward){
      this.trackList+=this.nextStack;
      this.backward = false;
     }
     this.playlists = this.playlistFullStack.slice(this.trackList, this.trackList+=this.nextStack)
     this.forwardDisabled = this.validateNext;
     this.backwarDdisabled = this.validatePrevious;
     if(this.validateNext){
          this.trackList = this.playlistFullStack.length-this.playlists.length;
          this.forward = false;
     }
    
  }

  previous(){
    console.log(this.trackList);
    this.backward = true;
    if(this.forward){
      this.trackList-=this.nextStack;
      this.forward = false;
    }
    this.playlists = this.playlistFullStack.slice(this.trackList-this.nextStack, this.trackList);
    this.trackList-=this.nextStack
    this.forwardDisabled = this.validateNext;
    this.backwarDdisabled = this.validatePrevious;
    if(this.validatePrevious){
      this.trackList = 0;
      this.backward = false;
  }
}

private get validateNext(): boolean{
  return this.playlists.length === undefined || this.playlists.length === 0 || this.playlists.length < 3 || this.trackList >= this.playlistFullStack.length;
}

private get validatePrevious(): boolean{
  return this.playlists.length === undefined || this.playlists.length === 0 || this.trackList < 3;
}

}
