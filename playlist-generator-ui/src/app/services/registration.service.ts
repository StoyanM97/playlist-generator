import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';

import { User } from '../models/user';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class RegistrationService {
  
    private readonly HOST = 'http://localhost:8080';
    private readonly REGISTER_URL = this.HOST + '/api/register';
    
    httpOptions = {
        headers: new HttpHeaders({'Content-Type': 'application/json'})
    };

    constructor(private httpClient: HttpClient){}

    registerUser(username: string, password: string): Observable<User>{
    
        console.log(username+" "+ password);
     const registerUserObject = {
         username: username,
         password: password
     }
     
    return this.httpClient.post<User>(this.REGISTER_URL, registerUserObject, this.httpOptions);
    }

}