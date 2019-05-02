import { Component, OnInit } from '@angular/core';
import { Location } from '@angular/common';
import { User } from './models/user';
import { AuthenticationService } from './services/authentication.service';
import { SearchService } from './services/search.service';
import { Filter } from './models/Filter';
import { PlaylistService } from './services/playlist.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss'],
})
export class AppComponent implements OnInit {
  
  loggedUser: User;
  hasPlaylists: boolean;

  constructor(private location: Location, private authenticationService: AuthenticationService,
    private searchService: SearchService, private playlistService: PlaylistService){}

  ngOnInit(){
    this.authenticationService.currentUser.subscribe(currentUser => this.loggedUser = currentUser);
    this.playlistService.playlistExist.subscribe(exist => this.hasPlaylists = exist);

  }

  setSearchValue(value: string){
       this.searchService.setSearchWord(value);
  }

  filterByTitle(value: string){
    this.getFilterObject("Title", value);
  }

  filterByGenre(value: string){
    this.getFilterObject("Genre", value);
  }

  filterByUsername(value: string){
    this.getFilterObject("Username", value);
  }

  filterByDuration(value: string){
   this.getFilterObject("Duration", value);
  }

  getFilterObject(method: string, filterWord: string){
    var filter = new Filter();
    filter.method = method;
    filter.filterWord = filterWord;
    this.searchService.setFilterObject(filter);
  }

  lagout(){
    this.authenticationService.logout();
  }

   //we can use it to reload page or refresh app
  load() {
    location.reload();
    }
}

