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
      console.log(event.value.firstName);
      console.log(event.value.lastName);
      console.log(event.value.email);
      console.log(event.value.username);
      console.log(event.value.password);

    //   this.registrationService.getUser().subscribe(data => {
    //     console.log(data);
    // },error => {
    //     console.log(error);
    //     console.log(error.status + "  " + error.error.error);
    //   },
    //   () => {
    //     // No errors, route to new page
    //   }
    //     );

      this.registrationService.registerUser(event.value.username,event.value.password).subscribe(
        data=>{
           console.log(data);
        },
        error=>{
          console.log("This is the error " + error);
        },
        ()=>{
          console.log("No errors");
          alert('SUCCESS!');
          this.router.navigate(['/login-component']);
        });
  }

}
