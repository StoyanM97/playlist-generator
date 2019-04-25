import { Component, OnInit } from '@angular/core';
import { DomSanitizer } from '@angular/platform-browser';
import { User } from '../models/user';
import { AuthenticationService } from '../services/authentication.service';
import { UserService } from '../services/user.service';

@Component({
  selector: 'user',
  templateUrl: './user.component.html',
  styleUrls: ['./user.component.scss']
})
export class UserComponent implements OnInit {

  user: User;
  
  selectedFile: File = null;
  oldUsername: string;
  edditing: boolean = false;
  hasImage: boolean = false;

  constructor(private authenticationService: AuthenticationService, private userService: UserService,
              private domSanitizer: DomSanitizer) { 

  }

  ngOnInit() {
    
    this.authenticationService.currentUser.subscribe(currentUser => this.user = currentUser);
    console.log(this.user.username);
    this.oldUsername = this.user.username;
    if(this.user.avatar !== undefined && this.user.avatar !== null){
      console.log(" has image");
      this.hasImage = true;
    }
  }

  onUploadPicture(event){
    this.selectedFile = event.target.files[0];
    
    this.userService.uploadAvatar(this.user.username, this.selectedFile).subscribe(data => {
        console.log(data);
    },error => {
        console.log(error);
        console.log(error.status + "  " + error.error.error);
      },
      () => {
        // No errors, route to new page
      }
        );
  
 console.log(event);
}

onUsernameChange(value: string){
  this.user.username = value;
  
}
onFirstNameChange(value: string){
  this.user.firstName = value;
}

onLastNameChange(value: string){
  this.user.lastName = value;
}

onEmailChange(value: string){
  this.user.email = value;
}

doneEditting(){
    if(this.edditing){
      
    this.userService.editUser(this.user, this.oldUsername).subscribe(data => {
        console.log(data);
    },error => {
        console.log(error);
      },
      () => {
        // No errors, route to new page
        this.authenticationService.saveEditUser(this.user);
        //this.authenticationService.currentUser.subscribe(currentUser => this.loggedUser = currentUser);
      }
        );
    
    }
  
  this.edditing = !this.edditing;
}

}
