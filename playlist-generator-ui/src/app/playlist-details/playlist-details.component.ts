import { Component, OnInit, Input } from '@angular/core';
import { Playlist } from '../models/playlist';
import { ActivatedRoute, Router } from '@angular/router';
import { PlaylistService } from '../services/playlist.service';
import { DomSanitizer } from '@angular/platform-browser';
import { User } from '../models/user';
import { AuthenticationService } from '../services/authentication.service';
import { ConfirmationService } from '../confirmation-dialog/confirmation.service';

@Component({
  selector: 'playlist-details',
  templateUrl: './playlist-details.component.html',
  styleUrls: ['./playlist-details.component.scss']
})
export class PlaylistDetailsComponent implements OnInit {
  
  edditing: boolean;
  playUrl: boolean;
  loading: boolean;
  editDeleteButtonsDisabled: boolean;

  previewUrl: string;
  title: string;

  loggedUser: User;
  playlist: Playlist;
  playlistId: number;

  constructor(private route: ActivatedRoute, private playlistService: PlaylistService, 
    private domSanitizer: DomSanitizer, private router: Router, private authenticationService: AuthenticationService,
    private confirmationService: ConfirmationService) { 
      this.authenticationService.currentUser.subscribe(currentUser => this.loggedUser = currentUser);
      this.edditing = false;
      this.playUrl = false;
      this.loading = true;
      this.editDeleteButtonsDisabled = true;
    }

  ngOnInit() {
    if(this.route.snapshot.paramMap.has("playlistId")){
       this.playlistId = +this.route.snapshot.paramMap.get("playlistId");
       this.displayPlaylist(this.playlistId);
    }
  }

  displayPlaylist(playlistId: number){
  this.playlistService.getPlaylist(playlistId).subscribe(data => {
      this.playlist = data;
   },error => {
    alert("Error: " +error);
    },() => {
        if(this.loggedUser === null) {
          this.editDeleteButtonsDisabled = true;
        }
        else if(this.playlist.username === this.loggedUser.username || this.loggedUser.role === 'ROLE_ADMIN'){
          this.editDeleteButtonsDisabled = false;
        }
        this.loading = !this.loading;
     });
  }

  onTitleChange(value: string){
        this.title = value;
  }

play(value: string){
  this.playUrl = !this.playUrl;
  this.previewUrl = value;
}

handelStop(){
  this.playUrl = !this.playUrl;
}

editPlaylist(){
  this.edditing = !this.edditing;
  if(this.title){
    this.playlistService.editPlaylist(this.title, this.playlistId, this.loggedUser.username).subscribe(data => {
   },error => {
      alert("Error: " +error);
    },() => { 
       this.playlist.title = this.title;
       this.playlistService.updatePlaylistLocal(this.playlist);
    });
  }
}

deletePlaylist(){
  
  if(this.edditing){
    this.edditing = !this.edditing;
  }
  else{
    this.openConfirmationDialog();
  }
}

openConfirmationDialog() {
  this.confirmationService.confirm('Please confirm!', 'Do you really want to delete this playlist?')
  .then((confirmed) => {
    
    if(confirmed){
      this.playlistService.deletePlaylist(this.playlistId).subscribe(data => {
        
      },error => {
        alert("Error: " +error);
      },() => { 
        this.playlistService.deletePlaylist(this.playlistId);
        this.router.navigate(['/playlists-dashboard']);
      });
    }
  })
  .catch(() => console.log('Dialog closed'));
}

}
