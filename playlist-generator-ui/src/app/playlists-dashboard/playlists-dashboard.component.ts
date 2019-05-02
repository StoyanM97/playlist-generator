import { Component, OnInit } from '@angular/core';
import { Playlist } from '../models/playlist';
import { PlaylistService } from '../services/playlist.service';
import { Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { SearchService } from '../services/search.service';
import { Filter } from '../models/Filter';

@Component({
  selector: 'playlists-dashboard',
  templateUrl: './playlists-dashboard.component.html',
  styleUrls: ['./playlists-dashboard.component.scss']
})
export class PlaylistsDashboardComponent implements OnInit {
  
  subscriptions = new Subscription();
  playlistFullStack: Playlist[];
  playlists: Playlist[];
  
  hasPlaylists: boolean;
  forward: boolean;
  backward: boolean;
  trackList: number;
  nextStack: number;
  
  forwardDisabled: boolean;
  backwarDdisabled: boolean;

  constructor(private playlistService: PlaylistService, private router: Router, private searchService: SearchService) {}

  ngOnInit() {

    this.playlistService.playlistsExistInDB().subscribe(data => {
      console.log(data);
      this.hasPlaylists = data;
      this.playlistService.setPlaylistExistValue(this.hasPlaylists);
      if(this.hasPlaylists){
       
        this.playlistService.getPlaylists().subscribe(data => {
          console.log(data);
          this.playlistFullStack = data;
        },error => {
          console.log(error);
        },() => { 
           this.loadPlaylists();
        });
      }
      
     },error => {
      console.log(error);
    },() => { });
     
  }

  loadPlaylists(){
    this.trackList = 0;
    this.nextStack = 3;
    this.forward = false;
    this.backward = false;
    this.playlists = this.playlistFullStack.slice(this.trackList, this.trackList+=this.nextStack);
    this.forwardDisabled = this.validateNext;
    this.backwarDdisabled = true;

  }

  ngAfterViewInit(){
    this.subscriptions.add(this.searchService.filterObject.subscribe(filterObject =>{
      if(filterObject !== undefined && filterObject !== null){
        this.filter(filterObject);
      }
    }));

    
  }

  ngOnDestroy(){
      this.subscriptions.unsubscribe();
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

filter(filter: Filter){
   
  switch(filter.method) { 
    case "Title": { 
       this.filterByTitle(filter.filterWord);
       break; 
    } 
    case "Genre": { 
       this.filterByGenre(filter.filterWord);
       break; 
    } 
    case "Username": {
       this.filterByUsername(filter.filterWord); 
       break;    
    } 
    case "Duration": { 
       this.filterByDuration(filter.filterWord);
       break; 
    }  
    default: { 
       console.log("Invalid method"); 
       break;              
    } 

}
}


filterByTitle(value: string){
  this.playlistService.getPlaylistsFiletByTitile(value).subscribe(data => {
    console.log(data);
    this.playlistFullStack = data;
  },error => {
    console.log(error);
  },() => { 
    this.loadPlaylists();
  }); 
}

filterByGenre(value: string){
  this.playlistService.getPlaylistsFiletrByGenre(value).subscribe(data => {
    console.log(data);
    this.playlistFullStack = data;
  },error => {
    console.log(error);
  },() => { 
    this.loadPlaylists();
  }); 
}

filterByUsername(value: string){
  this.playlistService.getPlaylistsFilterByUsername(value).subscribe(data => {
    console.log(data);
    this.playlistFullStack = data;
  },error => {
    console.log(error);
  },() => { 
    this.loadPlaylists();
  }); 
}

filterByDuration(value: string){
  this.playlistService.getPlaylistsFilterByDuration(+value).subscribe(data => {
    console.log(data);
    this.playlistFullStack = data;
  },error => {
    console.log(error);
  },() => { 
    this.loadPlaylists();
  });  
}

}
