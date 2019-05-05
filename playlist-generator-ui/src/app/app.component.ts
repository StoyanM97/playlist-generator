import { Component, OnInit } from '@angular/core';
import { Location } from '@angular/common';
import { User } from './models/user';
import { AuthenticationService } from './services/authentication.service';
import { SearchService } from './services/search.service';
import { Filter } from './models/Filter';
import { PlaylistService } from './services/playlist.service';
import { UserService } from './services/user.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss'],
})
export class AppComponent implements OnInit {
  
  loggedUser: User;
  hasPlaylists: boolean;
  onUsersComponent: boolean;
  search: string;
  placeholder: string;

  constructor(private location: Location, private authenticationService: AuthenticationService,
    private searchService: SearchService, private playlistService: PlaylistService,
    private userService: UserService){}

  ngOnInit(){
    this.authenticationService.currentUser.subscribe(currentUser => this.loggedUser = currentUser);
    this.playlistService.playlistExist.subscribe(exist => this.hasPlaylists = exist);
    this.userService.onUsersComponent.subscribe(onComponent => this.onUsersComponent = onComponent);
    this.searchService.searchValueObservable.subscribe(value => this.search = value);
    this.searchService.placeholderValueObservable.subscribe(value => this.placeholder = value);
  }

  setSearchValue(value: string){
       this.searchService.setSearchWord(value);
  }

  refreshUsersComponent(){
      this.searchService.setRefreshStatus(true);
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

  refresh(){
    this.getFilterObject("Refresh", null);
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

   //we can use it to reload page
  load() {
    location.reload();
    }
}

