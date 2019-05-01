import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import {ScrollingModule} from '@angular/cdk/scrolling';
import {MatCardModule} from '@angular/material';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RegistrationComponent } from './registration/registration.component';
import { LoginComponent } from './login/login.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { PlaylistsDashboardComponent } from './playlists-dashboard/playlists-dashboard.component';
import { UserComponent } from './user/user.component';
import { PlaylistComponent } from './playlist/playlist.component';
import { JwtInterceptor } from './helpers/jwt.interceptor';
import { ErrorInterceptor } from './helpers/error.interceptor';
import { UsersComponent } from './users/users.component';
import { ConfirmationComponent } from './confirmation-dialog/confirmation.component';
import { ConfirmationService } from './confirmation-dialog/confirmation.service';
import { NotFoundComponent } from './not-found/not-found.component';
import { PercentageService } from './services/percentage.survice';
import { PlaylistDetailsComponent } from './playlist-details/playlist-details.component';
import { AdminComponent } from './admin/admin.component';
import { PlayerComponent } from './playlist-details/player/player.component';

@NgModule({
  declarations: [
    AppComponent,
    RegistrationComponent,
    LoginComponent,
    PlaylistsDashboardComponent,
    UserComponent,
    PlaylistComponent,
    UsersComponent,
    ConfirmationComponent,
    NotFoundComponent,
    PlaylistDetailsComponent,
    AdminComponent,
    PlayerComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    FormsModule,
    ScrollingModule,
    MatCardModule,
    ReactiveFormsModule,
    HttpClientModule,
    NgbModule.forRoot()
  ],
  providers: [
    ConfirmationService,
    PercentageService,
    { provide: HTTP_INTERCEPTORS, useClass: JwtInterceptor, multi: true },
    { provide: HTTP_INTERCEPTORS, useClass: ErrorInterceptor, multi: true }
  ],
  entryComponents: [ ConfirmationComponent ],
  bootstrap: [AppComponent]
})
export class AppModule { }
