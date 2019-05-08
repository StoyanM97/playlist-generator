
import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';

import { Observable } from 'rxjs';
import { AuthenticationService } from './authentication.service';

@Injectable({ providedIn: 'root' })
export class AdminService{

    private readonly HOST = 'http://localhost:8080';
    private readonly ADMIN_URL = this.HOST + '/api/admin/download';

    private readonly DOWNLOAD_GENRES_URL = this.ADMIN_URL + '/genres';
    private readonly SYNC_GENRES_URL = this.ADMIN_URL + '/sync/genres';
    private readonly GENRES_EXIST_URL = this.ADMIN_URL + '/genres/exist';

    private readonly DOWNLOAD_TRACKS_URL = this.ADMIN_URL + '/tracks';
    private readonly TRACKS_EXIST_URL = this.ADMIN_URL + '/tracks/exist';


    httpOptions = {
        headers: new HttpHeaders({'Content-Type': 'application/json'})
    };

    constructor(private httpClient: HttpClient, private authenticationService: AuthenticationService){}

    downloadGenres(): Observable<boolean> {
        return this.httpClient.get<boolean>(this.DOWNLOAD_GENRES_URL, {headers: this.authenticationService.getHeader()});
    }

    syncGenres(): Observable<boolean> {
        return this.httpClient.get<boolean>(this.SYNC_GENRES_URL, {headers: this.authenticationService.getHeader()});
    }
    genresExist(): Observable<boolean> {
        return this.httpClient.get<boolean>(this.GENRES_EXIST_URL, {headers: this.authenticationService.getHeader()});
    }
    downloadTracks(): Observable<boolean> {
        return this.httpClient.get<boolean>(this.DOWNLOAD_TRACKS_URL, {headers: this.authenticationService.getHeader()});
    }
    trackExist(): Observable<boolean> {
        return this.httpClient.get<boolean>(this.TRACKS_EXIST_URL, {headers: this.authenticationService.getHeader()});
    }
}