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

  constructor(private domSanitizer: DomSanitizer, private route: ActivatedRoute,) { }
  
  ngOnInit() {
    this.iframe = this.domSanitizer.bypassSecurityTrustResourceUrl(this.url);
    // if(this.route.snapshot.paramMap.has("url")){
    //   console.log(true);
    //   this.previewUrl = this.route.snapshot.paramMap.get("url");
    //   this.previewUrl = this.previewUrl.replace(/"/g, "");   
    //   console.log("This is the url " + this.previewUrl);
    //   this.iframe = this.domSanitizer.bypassSecurityTrustResourceUrl(this.previewUrl);
    //   console.log(this.iframe);
    
    // }
  }

  stopPlaying(){
    this.stop.emit();
}

}
