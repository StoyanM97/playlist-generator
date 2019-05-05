import { Component, OnInit, Injectable } from "@angular/core";
import { UserService } from '../services/user.service';
import { User, test} from '../models/user';
import { AuthenticationService } from '../services/authentication.service';
import { SearchService } from '../services/search.service';
import { Subscription } from 'rxjs';
import { ConfirmationService } from '../confirmation-dialog/confirmation.service';

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
    edditing: boolean;

    constructor(private userService: UserService, private authenticationService: AuthenticationService,
      private searchService: SearchService, private confirmationService: ConfirmationService){
       this.edditing = false;
       this.userService.setOnUsersComponent(true);
       this.searchService.setPlaceholderValue("Search By Username");
    }
     
    ngOnInit(){
        this.subscriptions.add(this.userService.getUsers().subscribe(data => {
          this.users = data.filter(user => user.username !== this.authenticationService.currentUserValue.username );
        }));   
    }

    ngAfterViewInit(){
      this.subscriptions.add(this.searchService.searchWord.subscribe(word =>{
        if(word !== undefined && word !== null && this.users !== undefined && this.users !== null){
          this.searchByNameUser(word);
        }
      }));

      this.subscriptions.add(this.searchService.refreshStatusObservable.subscribe(status => {
        if(status){
              this.searchService.setRefreshStatus(false);
              this.searchService.setSearchValue("");
              this.ngOnInit();
        }
      } ));
    }

    ngOnDestroy(){
        this.userService.setOnUsersComponent(false);
        this.subscriptions.unsubscribe();
    }

    editMode(user){
       this.edditing = !this.edditing;
      
       console.log(user)
       if(this.edditing){
        this.oldUsername = user.username;
        this.user = user;
       }
       else{
        this.handleEdit(user);  
       }
          
    }

    handleEdit(oldUser: User){
     
      this.userService.editUserByAdmin(this.user, this.oldUsername).subscribe(data => {
        },error => {
          alert("Error: "+ error);
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
       this.openConfirmationDialog(event.username);
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
    
    searchByNameUser(name: string){
      this.users = this.users.filter(user => user.username.toUpperCase() === name.toUpperCase());
    }

    openConfirmationDialog(username: string) {
      this.confirmationService.confirm('Please confirm!', 'Do you really want to delete the this user!')
      .then(confirmed => {
        if(confirmed){
          this.userService.deleteUser(username).subscribe(data => {
          },error => {
            alert("Error: "+ error);
          },
          () => {
            this.users = this.users.filter((user: User) => user.username !== username );
          });
        }
      })
      .catch(() => console.log('Dialog closed'));
    }  
    
}