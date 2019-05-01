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
  avatar: any;
  oldUsername: string;
  edditing: boolean = false;
  hasImage: boolean = false;

  constructor(private authenticationService: AuthenticationService, private userService: UserService,
              private domSanitizer: DomSanitizer) { 

  }

  ngOnInit() {
    
    this.authenticationService.currentUser.subscribe(currentUser => this.user = currentUser);
    this.oldUsername = this.user.username;
    if(this.user.avatar !== undefined && this.user.avatar !== null){
      this.hasImage = true;
    }

    console.log("user avatar " + this.authenticationService.currentUserValue.avatar);
  }

onUploadAvatar(event){
    this.selectedFile = event.target.files[0];
    console.log(event);
    this.userService.uploadAvatar(this.user.username, this.selectedFile).subscribe(data => {
        console.log(data);
    },error => {
        console.log(error);
      },() => { 
        this.saveAvatar(this.selectedFile);
      });
}

saveAvatar(avatarFile: File){

  var reader:FileReader = new FileReader();

  reader.onloadend = (e) => {
    this.avatar = reader.result;
    this.user.avatar = this.avatar.replace("data:image/png;base64,","");
    this.authenticationService.saveEditUser(this.user);
    //location.reload();
    this.ngOnInit();
  }
  reader.readAsDataURL(avatarFile);
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
      alert('Your profile has not been updated!');
      },
      () => {
        this.authenticationService.saveEditUser(this.user);
        alert('Your profile has been successfully updated!');
      }
        );
    
    }
  
  this.edditing = !this.edditing;
}

}
