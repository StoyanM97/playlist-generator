import { Component, OnInit, Injectable } from "@angular/core";
import { UserService } from '../services/user.service';
import { User, test} from '../models/user';
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
    username: string;
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
      
       console.log(user)
       if(this.edditing){
        this.oldUsername = user.username;
        this.username = user.username;
        this.user = user;
       }
       else{
        this.handleEdit(user);  
       }
          
    }

    handleEdit(oldUser: User){
     
      this.userService.editUserByAdmin(this.user, this.oldUsername).subscribe(data => {
            console.log(data);
        },error => {
            console.log(error);
          },
          () => {
            this.users = this.users.map(user => {
              if(user.username === oldUser.username){
                  user = Object.assign({}, user, this.user);
              }
              return user; });
                
          });
    }

    delete(event){
    if(this.edditing){
      this.edditing = !this.edditing;
    }
    else{
      this.userService.deleteUser(event.username).subscribe(data => {
        console.log(data);
    },error => {
        console.log(error);
      },
      () => {
        this.users = this.users.filter((user: User) => user.username !== event.username );
      });
    }
    
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
     this.user.role = value;
    }
    
    //TODO add another array
    searchByNameUser(name: string){
      this.users = this.users.filter(user => user.username.toUpperCase() === name.toUpperCase());
    }
    
}