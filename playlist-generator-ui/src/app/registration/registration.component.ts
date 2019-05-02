import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

// import custom validator to validate that password and confirm password fields match
import { MustMatch } from '../helpers/must-match.validator';
import { Router } from '@angular/router';
import { RegistrationService } from '../services/registration.service';
import { AuthenticationService } from '../services/authentication.service';
import { User } from '../models/user';
import { UserService } from '../services/user.service';

@Component({
  selector: 'registration',
  templateUrl: './registration.component.html',
  styleUrls: ['./registration.component.scss']
})
export class RegistrationComponent implements OnInit {

  registerForm: FormGroup;
  loggedUser: User;

  constructor(private formBuilder: FormBuilder, private router: Router, private registrationService: RegistrationService, 
    private authenticationService: AuthenticationService, private userService: UserService) { 
      this.authenticationService.currentUser.subscribe(currentUser => this.loggedUser = currentUser);
    }

  ngOnInit() {
    this.registerForm = this.formBuilder.group({
      firstName: ['', [Validators.required, Validators.minLength(2), Validators.maxLength(15)]],
      lastName: ['', [Validators.required, Validators.minLength(2), Validators.maxLength(15)]],
      email: ['', [Validators.required, Validators.email]],
      role: [''],
      username: ['', [Validators.required, Validators.minLength(5), Validators.maxLength(10)]],
      password: ['', [Validators.required, Validators.pattern(/^(?=.*\d)(?=.*[a-z])(?=.*[A-Z]).{8,16}(?!.*\s).*$/)]],
      confirmPassword: ['', Validators.required]
  }, {
      validator: MustMatch('password', 'confirmPassword')
  });
  }

   // convenience getter for easy access to form fields
  get field() { return this.registerForm.controls; }

  onSubmit(event) {
      // stop here if form is invalid
      console.log(this.registerForm.invalid);
      if (this.registerForm.invalid) {
          return;
      }
      
      if(this.loggedUser && this.loggedUser.role === "ROLE_ADMIN"){
        
          this.userService.createUserByAdmin(event.value.username,event.value.password, 
            event.value.email, event.value.firstName, event.value.lastName, event.value.role).subscribe(
            data=>{
               console.log(data);
            },
            error=>{
              console.log("This is the error " + error);
              alert('Error! User was not Created!');
            },
            ()=>{
              alert('User Successful Created!');
              this.router.navigate(['/playlists-dashboard']);
            });
             
      }
      else{
        this.registrationService.registerUser(event.value.username,event.value.password, 
          event.value.email, event.value.firstName, event.value.lastName).subscribe(
          data=>{
             console.log(data);
          },
          error=>{
            console.log("This is the error " + error);
          },
          ()=>{
            alert('Successful Registration!\nYou will be Redirect to Login Page!');
            this.router.navigate(['/login']);
          });
      }
      
  }

}
