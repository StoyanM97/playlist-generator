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
  role: string;
  selectedFile: File = null;
  avatar: any;
  edditing: boolean = false;
  hasImage: boolean = false;

  constructor(private authenticationService: AuthenticationService, private userService: UserService,
              private domSanitizer: DomSanitizer) { 

  }

  ngOnInit() {
    
    this.authenticationService.currentUser.subscribe(currentUser => 
      { this.user = currentUser
       this.oldUser = this.getCloneUser(currentUser)
       this.displayRole(this.user.role);
      });
    if(this.user.avatar !== undefined && this.user.avatar !== null){
      this.hasImage = true;
    }
  }

onUploadAvatar(event){
    this.selectedFile = event.target.files[0];
    if(this.checkImageType(this.selectedFile)){
      this.userService.uploadAvatar(this.user.username, this.selectedFile).subscribe(data => {
        console.log(data);
    },error => {
      alert("Error: "+ error);
      },() => { 
        this.saveAvatar(this.selectedFile);
      });
    }
    else{
      alert("Image must be a png format!");
    }
   
}

checkImageType(file: File): boolean{
 return file.type.split("/")[file.type.split("/").length-1] === "png";
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
      alert("Error: "+ error);
      },
      () => {
        this.authenticationService.saveEditUser(this.user);
      });
    
    }
  
  this.edditing = !this.edditing;
}

cansel(){
  this.edditing = !this.edditing;
}

displayRole(roleIn: string){
   switch(roleIn){
      case "ROLE_USER":{
        this.role = "User";
        break; 
      }
      case "ROLE_ADMIN":{
        this.role = "Administrator"
        break;
      }
      default:{
         this.role = "No role"
      }
   }
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
