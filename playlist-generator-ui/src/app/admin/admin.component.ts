import { Component, OnInit } from '@angular/core';
import { AdminService } from '../services/admin.servvice';

@Component({
  selector: 'admin',
  templateUrl: './admin.component.html',
  styleUrls: ['./admin.component.scss']
})
export class AdminComponent implements OnInit {
  
  genresExist: boolean = false;
  tracksExist: boolean = false;
  sync: boolean;

  constructor(private adminService: AdminService) { }

  ngOnInit() {
     this.checkGenresExist();
     this.checkIfTrackExist();
  }

  checkIfTrackExist(){
    this.adminService.trackExist().subscribe(data => {
      console.log(data);
      this.tracksExist = data;
    },error => {
      console.log(error);
    },() => {});
  
  }

  checkGenresExist(){
    this.adminService.genresExist().subscribe(data => {
      console.log(data);
      this.genresExist= data;
   },error => {
      console.log(error);
    },() => { });
  
  }

  downloadGenres(){
     
    this.adminService.downloadGenres().subscribe(data => {
      console.log(data);
  },error => {
      console.log(error);
    },() => { 
      alert("Genres were downloaded!");
      this.genresExist = true;
    });
  
  }

  downloadTracks(){
    
    this.adminService.downloadTracks().subscribe(data => {
      console.log(data);
  },error => {
      console.log(error);
    },() => { 
      alert("Tracks were downloaded!");
      this.tracksExist = true;
    });
  
  }

  syncGenres(){
    this.adminService.syncGenres().subscribe(data => {
      console.log(data);
      this.sync = data;
  },error => {
      console.log(error);
    },() => { 
      alert(this.sync ? "Genres were sync!" :  "No new genres to be added!");
    });
  }
}
