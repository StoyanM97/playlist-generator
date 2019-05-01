import { Component, OnInit, Input } from '@angular/core';
import { Playlist } from '../models/playlist';
import { ActivatedRoute } from '@angular/router';
import { PlaylistService } from '../services/playlist.service';
import { DomSanitizer } from '@angular/platform-browser';

@Component({
  selector: 'playlist-details',
  templateUrl: './playlist-details.component.html',
  styleUrls: ['./playlist-details.component.scss']
})
export class PlaylistDetailsComponent implements OnInit {
  

  editting: boolean = false;
  playUrl: boolean = false;
  previewUrl: string;

  playlist: Playlist;
  playlistId: number;

  constructor(private route: ActivatedRoute, private playlistService: PlaylistService, private domSanitizer: DomSanitizer) { }

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
      
    });
  }
  onTitleChange(value: string){

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

}
