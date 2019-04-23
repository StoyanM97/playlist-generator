import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AuthenticationService } from '../services/authentication.service';
import { first } from 'rxjs/operators';
import { Router } from '@angular/router';

@Component({
  selector: 'login-component',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {
 
  registerForm: FormGroup;

  error = '';
  loading = false;

  constructor(private formBuilder: FormBuilder, private router: Router, private authenticationService: AuthenticationService) { }

  ngOnInit() {
    this.registerForm = this.formBuilder.group({
      username: ['', Validators.required],
      password: ['', Validators.required]
  });

   // reset login status
   this.authenticationService.logout();
  }

  // convenience getter for easy access to form fields
  get field() { return this.registerForm.controls; }

   onSubmit(event) {
       // stop here if form is invalid
       if (this.registerForm.invalid) {
           return;
       }
       this.loading = true;
       console.log(event.value.username);
       console.log(event.value.password);
       this.authenticationService.login(event.value.username, event.value.password)
            .pipe()
            .subscribe(
                data => {
                    alert('SUCCESS!');
                    this.router.navigate(['/dashboard']);
                },
                error => {
                    this.error = error;
                    this.loading = false;
                });
   }

}
