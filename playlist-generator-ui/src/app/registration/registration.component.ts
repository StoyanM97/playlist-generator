import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

// import custom validator to validate that password and confirm password fields match
import { MustMatch } from '../helpers/must-match.validator';
import { Router } from '@angular/router';
import { RegistrationService } from '../services/registration.service';

@Component({
  selector: 'registration',
  templateUrl: './registration.component.html',
  styleUrls: ['./registration.component.scss']
})
export class RegistrationComponent implements OnInit {

  registerForm: FormGroup;

  constructor(private formBuilder: FormBuilder, private router: Router, private registrationService: RegistrationService) { }

  ngOnInit() {
    this.registerForm = this.formBuilder.group({
      firstName: ['', [Validators.required, Validators.minLength(2), Validators.maxLength(15)]],
      lastName: ['', [Validators.required, Validators.minLength(2), Validators.maxLength(15)]],
      email: ['', [Validators.required, Validators.email]],
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
      if (this.registerForm.invalid) {
          return;
      }
       
      this.registrationService.registerUser(event.value.username,event.value.password, 
        event.value.email, event.value.firstName, event.value.lastName).subscribe(
        data=>{
           console.log(data);
        },
        error=>{
          console.log("This is the error " + error);
        },
        ()=>{
          alert('successful Registration! Redirect to Login Page!');
          this.router.navigate(['/login']);
        });
  }

}
