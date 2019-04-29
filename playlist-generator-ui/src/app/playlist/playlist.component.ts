import { Component, OnInit } from '@angular/core';
import { PercentageService } from '../services/percentage.survice';
import { FormGroup, FormBuilder, Validators, FormControl } from '@angular/forms';
import { MustMatch } from '../helpers/must-match.validator';
import { Router } from '@angular/router';

@Component({
  selector: 'playlist',
  templateUrl: './playlist.component.html',
  styleUrls: ['./playlist.component.scss']
})
export class PlaylistComponent implements OnInit {
  
  theCheckbox = false;
  values: number[];
  playlistForm: FormGroup;
  
  constructor(private percentageService: PercentageService, private formBuilder: FormBuilder, private router: Router ) { 
    this.values = this.percentageService.getValues;
  }

  ngOnInit() {
    this.playlistForm = this.formBuilder.group({
      title: ['', [Validators.required, Validators.minLength(2), Validators.maxLength(15)]],
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

  onGeneratePlaylist(event){
    if (this.playlistForm.invalid) {
      return;
    }

    if(event.value.popGenrePercentage + event.value.danceGenrePercentage + event.value.rockGenrePercentage > 100 ){
      alert('Total genre percentage can not be more than 100%!');
    }
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

  }

  cancel(){
    this.router.navigate(['/playlists-dashboard']);
  }

}
