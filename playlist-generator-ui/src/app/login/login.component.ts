import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AuthenticationService } from '../services/authentication.service';
import { Router } from '@angular/router';

@Component({
  selector: 'login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {
 
  loginForm: FormGroup;

  error: string;
  loading: boolean = false;

  constructor(private formBuilder: FormBuilder, private router: Router, private authenticationService: AuthenticationService) { }

  ngOnInit() {
    this.loginForm = this.formBuilder.group({
      username: ['', Validators.required],
      password: ['', Validators.required]
  });
    
   // reset login status
   this.authenticationService.logout();
  }

  // convenience getter for easy access to form fields
  get field() { return this.loginForm.controls; }

   onSubmit(event) {
       // stop here if form is invalid
       if (this.loginForm.invalid) {
           return;
       }
       this.loading = true;
       this.authenticationService.login(event.value.username, event.value.password)
       .subscribe(
        data => { console.log(data);},
        error => {console.log(error);
                    this.error = error;
                    this.loading = false;},
        ()=>{ this.router.navigate(['/playlists-dashboard']);}
        );
   }
}
