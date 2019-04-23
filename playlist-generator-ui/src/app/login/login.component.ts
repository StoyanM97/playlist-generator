import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

@Component({
  selector: 'login-component',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {
 
  registerForm: FormGroup;

  error = '';
  loading = false;

  constructor(private formBuilder: FormBuilder) { }

  ngOnInit() {
    this.registerForm = this.formBuilder.group({
      username: ['', Validators.required],
      password: ['', Validators.required]
  });
  }

  // convenience getter for easy access to form fields
  get field() { return this.registerForm.controls; }

   onSubmit(event) {
       // stop here if form is invalid
       if (this.registerForm.invalid) {
           return;
       }
       this.loading = true;;
       console.log(event.value.username);
       console.log(event.value.password);

       alert('SUCCESS!');
   }

}
