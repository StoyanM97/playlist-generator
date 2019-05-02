import { Component, OnInit } from '@angular/core';
import { PercentageService } from '../services/percentage.survice';
import { FormGroup, FormBuilder, Validators, FormControl } from '@angular/forms';
import { Router } from '@angular/router';
import { PlaylistService } from '../services/playlist.service';
import { PlaylistGenerator, Genre } from '../models/playlistGenerator';
import { AuthenticationService } from '../services/authentication.service';

@Component({
  selector: 'playlist',
  templateUrl: './playlist.component.html',
  styleUrls: ['./playlist.component.scss']
})
export class PlaylistComponent implements OnInit {
  
  theCheckbox = false;
  loading = false;
  values: number[];
  playlistForm: FormGroup;
  
  constructor(private percentageService: PercentageService, private formBuilder: FormBuilder, 
    private router: Router, private playlistService: PlaylistService, private authenticationService: AuthenticationService) { 
    this.values = this.percentageService.getValues;
  }

  ngOnInit() {
    this.playlistForm = this.formBuilder.group({
      title: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(10)]],
      fromPoint: ['', Validators.required],
      toPoint: ['', Validators.required],
      popGenre: [false],
      popGenrePercentage: [0],
      danceGenre: [false],
      danceGenrePercentage: [0],
      rockGenre: [false],
      rockGenrePercentage: [0],
      useTopTracks: [false],
      allowSameArtists: [false]
  });
  }

    // convenience getter for easy access to form fields
    get field() { return this.playlistForm.controls; }

    cancel(){this.router.navigate(['/playlists-dashboard']);}

    generatePlaylist(playlistGenerator: PlaylistGenerator){
      this.loading = !this.loading;
      this.playlistService.createPlaylist(playlistGenerator).subscribe(data => {
        console.log(data);
      },error => {
        console.log(error);
        this.loading = !this.loading;
      },() => { 
        alert("Playlist created!");
        this.loading = !this.loading;
        this.router.navigate(['/playlists-dashboard']);
      });
    }

    onGeneratePlaylist(event){

      console.log(event.value.title);
      console.log(event.value.fromPoint);
      console.log(event.value.toPoint);
      console.log("Pop "+ event.value.popGenre);
      console.log("Pop %"+ event.value.popGenrePercentage);
      console.log("Dance "+event.value.danceGenre);
      console.log("Dance %"+event.value.danceGenrePercentage);
      console.log("Rock "+event.value.rockGenre);
      console.log("Rock %"+event.value.rockGenrePercentage);
      console.log("Top Tracks "+event.value.useTopTracks);
      console.log("Same artists "+event.value.allowSameArtists);

    if (this.playlistForm.invalid) {
      return;
    }
    var totalPercentage = event.value.popGenrePercentage + event.value.danceGenrePercentage + event.value.rockGenrePercentage;
    
    if(totalPercentage !== 0 && totalPercentage !== 100){
      alert('Total genres percentage must be 100%!');
      return;
    }

    var playlistGenerator = new PlaylistGenerator();
    playlistGenerator.title = event.value.title;
    playlistGenerator.travelFrom = event.value.fromPoint;
    playlistGenerator.travelTo = event.value.toPoint;
    playlistGenerator.username = this.authenticationService.currentUserValue.username;
    playlistGenerator.allowSameArtists = event.value.allowSameArtists;
    playlistGenerator.useTopTracks = event.value.useTopTracks;
    var genres: Genre[] = [];

    if( totalPercentage === 100 ){
      var genrePop = new Genre();
      if(event.value.popGenre){
        genrePop.genre = "Pop";
        genrePop.percentage = event.value.popGenrePercentage;
      }
      genres.push(genrePop);

      var genreDance = new Genre();
      if(event.value.danceGenre){
        genreDance.genre = "Dance";
        genreDance.percentage = event.value.danceGenrePercentage;
      }
      genres.push(genreDance);

      var genreRock = new Genre();
      if(event.value.rockGenre){
        genreRock.genre = "Rock";
        genreRock.percentage = event.value.rockGenrePercentage;
      }
      genres.push(genreRock);

    }
    else if(totalPercentage === 0){
        
      if(event.value.popGenre && !event.value.danceGenre && !event.value.rockGenre){
        var genre = new Genre();
        genre.genre = "Pop"
        genre.percentage = 100;
        genres.push(genre);
      }
      else if(!event.value.popGenre && event.value.danceGenre && !event.value.rockGenre){ 
        var genre = new Genre();
        genre.genre = "Dance"
        genre.percentage = 100;
        genres.push(genre);
      }
      else if(!event.value.popGenre && !event.value.danceGenre && event.value.rockGenre){ 
        var genre = new Genre();
        genre.genre = "Rock"
        genre.percentage = 100;
        genres.push(genre);
      }
      else if(event.value.popGenre && event.value.danceGenre && !event.value.rockGenre){ 
        var genrePop = new Genre();
        genrePop.genre = "Pop"
        genrePop.percentage = 50;
        genres.push(genrePop);

        var genreDance = new Genre();
        genreDance.genre = "Dance"
        genreDance.percentage = 50;
        genres.push(genreDance);
      }
      else if(event.value.popGenre && !event.value.danceGenre && event.value.rockGenre){ 
        var genrePop = new Genre();
        genrePop.genre = "Pop"
        genrePop.percentage = 50;
        genres.push(genrePop);
        
        var genreRock = new Genre();
        genreRock.genre = "Rock"
        genreRock.percentage = 50;
        genres.push(genreRock);
      }
      else if(!event.value.popGenre && event.value.danceGenre && event.value.rockGenre){ 
        var genreDance = new Genre();
        genreDance.genre = "Dance"
        genreDance.percentage = 50;
        genres.push(genreDance);
        
        var genreRock = new Genre();
        genreRock.genre = "Rock"
        genreRock.percentage = 50;
        genres.push(genreRock);
      }
      else if( (event.value.popGenre && event.value.danceGenre && event.value.rockGenre) || (!event.value.popGenre && !event.value.danceGenre && !event.value.rockGenre)) { 
        var genrePop = new Genre();
        genrePop.genre = "Pop"
        genrePop.percentage = 100/3;
        genres.push(genrePop);

        var genreDance = new Genre();
        genreDance.genre = "Dance"
        genreDance.percentage = 100/3;
        genres.push(genreDance);
        
        var genreRock = new Genre();
        genreRock.genre = "Rock"
        genreRock.percentage = 100/3;
        genres.push(genreRock);
      }
    }

    playlistGenerator.genres = genres;
    this.generatePlaylist(playlistGenerator);   
  }

}
