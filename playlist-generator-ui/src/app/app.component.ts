import { Component, OnInit } from '@angular/core';
import { Location } from '@angular/common';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss'],
})
export class AppComponent implements OnInit {

  isLogged: boolean = false;

  constructor(private location: Location){}

  ngOnInit(){
  }

  lagout(){
    //clean local storage
    localStorage.clear();
    this.isLogged = false;
     //reload page or refresh app
     this.load();
     
  }

  load() {
    location.reload();
    }
}
