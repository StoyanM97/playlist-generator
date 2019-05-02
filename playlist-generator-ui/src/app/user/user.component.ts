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
  oldUser: User;
  selectedFile: File = null;
  avatar: any;
  edditing: boolean = false;
  hasImage: boolean = false;

  constructor(private authenticationService: AuthenticationService, private userService: UserService,
              private domSanitizer: DomSanitizer) { 

  }

  ngOnInit() {
    
    this.authenticationService.currentUser.subscribe(currentUser => 
      {this.user = currentUser
       this.oldUser = this.getCloneUser(currentUser)});
    if(this.user.avatar !== undefined && this.user.avatar !== null){
      this.hasImage = true;
    }
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
    this.ngOnInit();
  }
  reader.readAsDataURL(avatarFile);
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
      
    this.userService.editUser(this.user).subscribe(data => {
        console.log(data);
    },error => {
      this.authenticationService.saveEditUser(this.oldUser);
      alert('Your profile has not been updated!\n'+ error);
      },
      () => {
        this.authenticationService.saveEditUser(this.user);
      }
        );
    
    }
  
  this.edditing = !this.edditing;
}

getCloneUser(userIn: User): User{
   var userClone = new User();
   userClone.firstName = userIn.firstName;
   userClone.lastName = userIn.lastName;
   userClone.username = userIn.username;
   userClone.role = userIn.role;
   userClone.email= userIn.email;
   userClone.avatar = userIn.avatar;
   userClone.token = userIn.token;
   
   return userClone;
}

}
