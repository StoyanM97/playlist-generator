import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';

import { User } from '../models/user';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class UserService {
  
    private readonly HOST = 'http://localhost:8080';
    private readonly USER_URL = this.HOST + '/api/user';
    private readonly ADMIN_URL = this.HOST + '/api/admin';

    private readonly EDIT_USER = this.USER_URL + '/edit';
    private readonly UPLOAD_AVATAR = this.USER_URL + '/upload/avatar';

    private readonly GET_USERS = this.ADMIN_URL + '/users';
    private readonly DELETE_USER = this.ADMIN_URL + '/delete/user';
    private readonly EDIT_USER_BY_ADMIN = this.ADMIN_URL + '/edit/user';
    private readonly CREATE_USER_BY_ADMIN = this.ADMIN_URL + '/create/user';
    
    httpOptions = {
        headers: new HttpHeaders({'Content-Type': 'application/json'})
    };

    constructor(private httpClient: HttpClient){}
    
    getUsers(): Observable<User[]> {
      return this.httpClient.get<User[]>(this.GET_USERS);
  }
    uploadAvatar(username: string, avatar: File): Observable<{}>{
        const formdata: FormData = new FormData();
        formdata.append('file', avatar);
        formdata.append('username', username);
     
        return this.httpClient.post(this.UPLOAD_AVATAR, formdata);
    }

    editUser(user: User, oldUsername: string): Observable<{}>{
        const editUser = 
        {   username: user.username, 
            firstName: user.firstName,
            lastName: user.lastName,
            email: user.email,
            oldUsername: oldUsername
        };
      return this.httpClient.put<User>(this.EDIT_USER, editUser, this.httpOptions);
    }

    deleteUser(username: string): Observable<{}> {
        const url = `${this.DELETE_USER}/${username}`;
        return this.httpClient.delete(url, this.httpOptions);
    }
  
    editUserByAdmin(user: User, oldUsername: string): Observable<{}>{
      const editUser = 
     {   username: user.username,
         firstName: user.firstName,
         lastName: user.lastName,
         email: user.email,
         role: user.role,
         oldUsername: oldUsername
     };
   return this.httpClient.put<User>(this.EDIT_USER_BY_ADMIN, editUser, this.httpOptions);
  }

  createUserByAdmin(username: string, password: string, firstName: string, lastName: string, email: string, userRole: string,): Observable<{}>{
    const createUser = 
    { username: username, password: password, firstName: firstName, lastName: lastName, email: email, role: userRole };
  return this.httpClient.post<User>(this.CREATE_USER_BY_ADMIN, createUser, this.httpOptions); 
  }

}