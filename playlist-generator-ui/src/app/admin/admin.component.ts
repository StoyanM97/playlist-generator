import { Component, OnInit } from '@angular/core';
import { AdminService } from '../services/admin.service';

@Component({
  selector: 'admin',
  templateUrl: './admin.component.html',
  styleUrls: ['./admin.component.scss']
})
export class AdminComponent implements OnInit {
  
  genresExist: boolean = false;
  tracksExist: boolean = false;
  sync: boolean;
  loading: boolean = false;

  constructor(private adminService: AdminService) { }

  ngOnInit() {
     this.checkGenresExist();
     this.checkIfTrackExist();
  }

  checkIfTrackExist(){
    this.adminService.trackExist().subscribe(data => {
      this.tracksExist = data;
    },error => {
      alert("Error: "+ error);
    },() => {});
  
  }

  checkGenresExist(){
    this.adminService.genresExist().subscribe(data => {
      this.genresExist= data;
    },error => {
      alert("Error: "+ error);
    },() => { });
  
  }

  downloadGenres(){
    this.loading = !this.loading;
    this.adminService.downloadGenres().subscribe(data => {
    },error => {
    this.loading = !this.loading;
    alert("Error: "+ error);
    },() => { 
      this.loading = !this.loading;
      alert("Genres were downloaded!");
      this.genresExist = true;
    });
  
  }

  downloadTracks(){
    this.loading = !this.loading;
    this.adminService.downloadTracks().subscribe(data => { 
    },error => {
      this.loading = !this.loading;
      alert("Error: "+ error);
    },() => { 
      this.loading = !this.loading;
      alert("Tracks were downloaded!");
      this.tracksExist = true;
    });
  
  }

  syncGenres(){
    this.loading = !this.loading;
    this.adminService.syncGenres().subscribe(data => {
      this.sync = data;
    },error => {
    this.loading = !this.loading;
    alert("Error: "+ error);
    },() => { 
      this.loading = !this.loading;
      alert(this.sync ? "Genres were sync!" :  "No new genres to be added!");
    });
  }
}
