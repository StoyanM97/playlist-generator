import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { User } from '../models/user';

@Injectable({ providedIn: 'root' })
export class AuthenticationService {
    // There are two properties exposed by the authentication service for accessing
    //the currently logged in user. The currentUser observable can be used when you 
    //want a component to reactively update when a user logs in or out, for example 
    //in the app.component.ts so it can show/hide the main nav bar when the user logs in/out. 
    //The currentUserValue property can be used when you just want to get the current value 
    //of the logged in user but don't need to reactively update when it changes, for example 
    //in the auth.guard.ts which restricts access to routes by checking if the user is currently logged in.
    private currentUserSubject: BehaviorSubject<User>;
    public currentUser: Observable<User>;

    constructor(private http: HttpClient) {

        this.currentUserSubject = new BehaviorSubject<User>(JSON.parse(localStorage.getItem('currentUser')));
        this.currentUser = this.currentUserSubject.asObservable();
    }

    public get currentUserValue(): User {
        return this.currentUserSubject.value;
    }

    login(username: string, password: string) {

        // return this.http.post<User>(`/users/authenticate`, { username, password })
        //     .pipe(map(user => {
        //         // login successful if there's a jwt token in the response
        //         if (user && user.token) {
        //             // store user details and jwt token in local storage to keep user logged in between page refreshes
        //             localStorage.setItem('currentUser', JSON.stringify(user));
        //             this.currentUserSubject.next(user);
        //         }

        //         return user;
        //     }));
    }

    logout() {
        //clean local storage
        localStorage.clear();
        //set user to null
        this.currentUserSubject.next(null);
    }
}