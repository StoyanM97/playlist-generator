import { Component, OnInit, Input } from '@angular/core';
import { Playlist } from '../models/playlist';
import { ActivatedRoute, Router } from '@angular/router';
import { PlaylistService } from '../services/playlist.service';
import { DomSanitizer } from '@angular/platform-browser';
import { User } from '../models/user';
import { AuthenticationService } from '../services/authentication.service';

@Component({
  selector: 'playlist-details',
  templateUrl: './playlist-details.component.html',
  styleUrls: ['./playlist-details.component.scss']
})
export class PlaylistDetailsComponent implements OnInit {
  

  edditing: boolean = false;
  playUrl: boolean = false;
  editButtonDisabled: boolean = true;
  previewUrl: string;
  title: string;

  playlist: Playlist;
  playlistId: number;

  loggedUser: User;

  constructor(private route: ActivatedRoute, private playlistService: PlaylistService, 
    private domSanitizer: DomSanitizer, private router: Router, private authenticationService: AuthenticationService) { 
      this.authenticationService.currentUser.subscribe(currentUser => this.loggedUser = currentUser);
    }

  ngOnInit() {
    if(this.route.snapshot.paramMap.has("playlistId")){
      console.log(true);
    this.playlistId = +this.route.snapshot.paramMap.get("playlistId");
    this.setPlaylist(this.playlistId);
    }
  }

  setPlaylist(playlistId: number){
  this.playlistService.getPlaylist(playlistId).subscribe(data => {
      console.log(data);
      this.playlist = data;
   },error => {
      console.log(error);
    },() => {
        if(this.playlist.username === this.loggedUser.username || this.loggedUser.role === 'ROLE_ADMIN'){
          this.editButtonDisabled = false;
        }
     });
  }
  onTitleChange(value: string){
        this.title = value;
  }

  getFilter(value: string) {
    return JSON.stringify(value);
   
}

play(value: string){
  this.playUrl = !this.playUrl;
  console.log(value);
  this.previewUrl = value;
}

handelStop(){
  this.playUrl = !this.playUrl;
}

editPlaylist(){
  this.edditing = !this.edditing;
  if(this.title){
    console.log("edit title");
    this.playlistService.editPlaylist(this.title, this.playlistId, this.loggedUser.username).subscribe(data => {
      console.log(data);
   },error => {
      console.log(error);
    },() => { 
       this.playlist.title = this.title;
       this.playlistService.updatePlaylistLocal(this.playlist);
    });
  }
}

deletePlaylist(){
  this.playlistService.deletePlaylist(this.playlistId).subscribe(data => {
    console.log(data);
    
  },error => {
    console.log(error);
  },() => { 
    this.playlistService.deletePlaylist(this.playlistId);
    this.router.navigate(['/playlists-dashboard']);
  });
}

}
