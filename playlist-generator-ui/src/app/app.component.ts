import { Component, OnInit } from '@angular/core';
import { Location } from '@angular/common';
import { User } from './models/user';
import { AuthenticationService } from './services/authentication.service';
import { Observable, BehaviorSubject } from 'rxjs';
import { SearchService } from './services/search.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss'],
})
export class AppComponent implements OnInit {
  
  loggedUser: User;

  constructor(private location: Location, private authenticationService: AuthenticationService,
    private searchService: SearchService){}

  ngOnInit(){
    this.authenticationService.currentUser.subscribe(currentUser => this.loggedUser = currentUser);
  }

  setSearchValue(value: string){
       this.searchService.setSearchWord(value);
  }

  lagout(){
    this.authenticationService.logout();
  }

   //we can use it to reload page or refresh app
  load() {
    location.reload();
    }
}

