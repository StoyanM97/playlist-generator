import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';

import { User } from '../models/user';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class RegistrationService {
  
    private readonly HOST = '//localhost:8080';
    private readonly REGISTER_URL = '/api/register';
    
    httpOptions = {
        headers: new HttpHeaders({'Content-Type': 'application/json'})
    };

    constructor(private httpClient: HttpClient){}

    registerUser(username: string, password: string): Observable<any>{

     const registerUserObject = {
         username: username,
         password: password
     }
     
    return this.httpClient.post<any>(this.REGISTER_URL, registerUserObject, this.httpOptions);
    }
}