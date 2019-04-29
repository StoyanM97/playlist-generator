import { Component, OnInit, Injectable } from "@angular/core";
import { UserService } from '../services/user.service';
import { User } from '../models/user';
import { AuthenticationService } from '../services/authentication.service';
import { SearchService } from '../services/search.service';
import { Subscription } from 'rxjs';

@Component({ 
    selector: 'users',
    templateUrl: 'users.component.html' ,
    styleUrls: ['users.component.scss']
    
})

export class UsersComponent implements OnInit{
    
    private subscriptions = new Subscription();
    
    user: User;
    users: User[];
    oldUsername: string;
    edditing: boolean = false;
   


    constructor(private userService: UserService, private authenticationService: AuthenticationService,
      private searchService: SearchService){
     
    }
     
    ngOnInit(){
        this.subscriptions.add(this.userService.getUsers().subscribe(data => {
                    this.users = data.filter(user => user.username !== this.authenticationService.currentUserValue.username );
        }));   
    }

    ngAfterViewInit(){
      this.subscriptions.add(this.searchService.searchWord.subscribe(word =>{
        console.log(word);
        if(word !== undefined && word !== null && this.users !== undefined && this.users !== null){
          this.searchByNameUser(word);
        }
      }));
    }

    ngOnDestroy(){
        this.subscriptions.unsubscribe();
    }

    editMode(user){
       this.edditing = !this.edditing;
       if(this.edditing){
        this.oldUsername = this.user.username;
        this.user = user;
       }
       else{
        this.handleEdit(user);  
       }
          
    }

    handleEdit(oldUser: User){
      this.users = this.users.map(u => {
          if(u.username === oldUser.username){
              u = Object.assign({}, u, this.user);
          }
          return u;
      });

      this.userService.editUserByAdmin(this.user, this.oldUsername).subscribe(data => {
            console.log(data);
        },error => {
            console.log(error);
          },
          () => {
            this.authenticationService.saveEditUser(this.user);
          });
    }

    delete(event){
      this.users = this.users.filter((user: User) => user.username !== event.username );
      this.userService.deleteUser(event.username).subscribe(data => {
        console.log(data);
    },error => {
        console.log(error);
      },
      () => {});
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

    searchByNameUser(name: string){
      this.users = this.users.filter((user: User) => user.username.toUpperCase() === name.toUpperCase());
    }
    
}