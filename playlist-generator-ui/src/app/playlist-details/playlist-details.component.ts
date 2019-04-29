import { Component, OnInit, Input } from '@angular/core';
import { Playlist } from '../models/playlist';

@Component({
  selector: 'playlist-details',
  templateUrl: './playlist-details.component.html',
  styleUrls: ['./playlist-details.component.scss']
})
export class PlaylistDetailsComponent implements OnInit {
  

  editting: boolean = false;
  
  @Input()
  playlist: Playlist;

  constructor() { }

  ngOnInit() {
  }


  onTitleChange(value: string){

  }
  playPreview(value: string){

  }

}
