import { Component, OnInit } from '@angular/core';
import { Location } from '@angular/common';
import { User } from './models/user';
import { Router } from '@angular/router';
import { AuthenticationService } from './services/authentication.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss'],
})
export class AppComponent implements OnInit {
  
  loggedUser: User;
  
  constructor(private location: Location, private router: Router,
    private authenticationService: AuthenticationService){}

  ngOnInit(){
    this.authenticationService.currentUser.subscribe(currentUser => this.loggedUser = currentUser);
  }

  lagout(){
    this.authenticationService.logout();
    this.router.navigate(['/login']);
  }

   //we can use it to reload page or refresh app
  load() {
    location.reload();
    }
}

