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

  loading: boolean;
  noPlaylists: boolean;
  noResult: boolean;

  forward: boolean;
  backward: boolean;
  forwardDisabled: boolean;
  backwarDdisabled: boolean;

  trackList: number;
  nextStack: number;

  constructor(private playlistService: PlaylistService, private router: Router, private searchService: SearchService) {
    this.playlistService.setPlaylistExistValue(true);
    this.searchService.setPlaceholderValue("Search Playlist");
  
  }

  ngOnInit() { 
    
    this.loading = true;
    this.noPlaylists = false;
    this.noResult = false;

    this.playlistService.getPlaylists().subscribe(data => {
      this.playlistFullStack = data;
    },error => {
      alert("Error: " + error);
      this.loading = false;
    },() => { 
      console.log("Here");
       this.loading = false;
       this.noPlaylists = this.playlistFullStack.length === 0;
       this.loadPlaylists();
    });
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
    this.loading = false;
    this.playlistService.setPlaylistExistValue(false);
    this.subscriptions.unsubscribe();
  }

  showPlaylistDetails(value: Playlist){
    this.loading = false;
    this.router.navigate(['/playlist-details', value.playlistId]);
  }

  next(){
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
    case "Refresh": { 
      this.searchService.setSearchValue("");
      this.ngOnInit();
      break; 
   }  
    default: { 
       console.log("Invalid method"); 
       break;              
    } 

}
}


filterByTitle(value: string){
  this.loading = true;
  this.playlistService.getPlaylistsFiletByTitile(value).subscribe(data => {
    this.playlistFullStack = data;
  },error => {
    this.loading = !this.loading;
    alert(error);
  },() => { 
    this.setupView();
  }); 
}

filterByGenre(value: string){
  this.loading = true;
  this.playlistService.getPlaylistsFiletrByGenre(value).subscribe(data => {
    this.playlistFullStack = data;
  },error => {
    this.loading = !this.loading;
    alert(error);
  },() => { 
    this.setupView();
  }); 
}

filterByUsername(value: string){
  this.loading = true;
  this.playlistService.getPlaylistsFilterByUsername(value).subscribe(data => {
    this.playlistFullStack = data;
  },error => {
    this.loading = !this.loading;
    alert(error);
  },() => { 
    this.setupView();
  }); 
}

filterByDuration(value: string){
  this.loading = true;
  this.playlistService.getPlaylistsFilterByDuration(+value).subscribe(data => {
    this.playlistFullStack = data;
  },error => { 
    this.loading = !this.loading;
    alert(error);
  },() => { 
    this.setupView();
  });  
}

setupView(){
  this.loading = false;
  this.noResult = this.playlistFullStack.length === 0;
  this.loadPlaylists();
}

}
