import { Component, OnInit, Injectable } from "@angular/core";
import { UserService } from '../services/user.service';
import { User } from '../models/user';

@Component({ 
    selector: 'users',
    templateUrl: 'users.component.html' ,
    styleUrls: ['users.component.scss']
    
})

export class UsersComponent implements OnInit{
    
    user: User;
    users: User[];
    edditing: boolean = false;

    constructor(private userService: UserService){
     
    }
     
    ngOnInit(){     
         this.userService.getUsers().subscribe(data => {
                    console.log('this is the object ', data);
                    this.users = data;
        });  
    }

    editMode(user){
       this.edditing = !this.edditing;
       if(this.edditing){
        this.user = user;
       }
       else{
        this.handleEdit(user);  
       }
          
    }

    delete(event){
      this.users = this.users.filter((user: User) => user.username !== event.username );
      this.userService.deleteUser(event.username).subscribe(data => {
        console.log(data);
    },error => {
        console.log(error);
      },
      () => {
        // No errors, route to new page
      }
        );
    }

    handleEdit(oldUser: User){
      this.users = this.users.map((u: User) => {
          if(u.username === oldUser.username){
              u = Object.assign({}, u, this.user);
          }
          return u;
      });

      this.userService.editUserByAdmin(this.user).subscribe(data => {
            console.log(data);
        },error => {
            console.log(error);
            console.log(error.status + "  " + error.error.error);
          },
          () => {
            // No errors, route to new page
          }
            );
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

    onUserRoleChange(value: string){
      this.user.role = value.toUpperCase();
    }

    showByNameUser(name: string){
        this.users = this.users.filter((user: User) => user.username.toUpperCase() === name.toUpperCase());
    }
    
}