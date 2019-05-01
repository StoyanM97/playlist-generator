import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';
import { DomSanitizer } from '@angular/platform-browser';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'player',
  templateUrl: './player.component.html',
  styleUrls: ['./player.component.scss']
})
export class PlayerComponent implements OnInit {

  iframe: any;
  previewUrl: string;

  @Input()
  url: string;

  @Output()
  stop: EventEmitter<any> = new EventEmitter();

  constructor(private domSanitizer: DomSanitizer, private route: ActivatedRoute) { }
  
  ngOnInit() {
    this.iframe = this.domSanitizer.bypassSecurityTrustResourceUrl(this.url);
  }
  stopPlaying(){
    this.stop.emit();
}

}
