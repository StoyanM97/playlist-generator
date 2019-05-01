import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { LoginComponent } from './login/login.component';
import { RegistrationComponent } from './registration/registration.component';
import { PlaylistsDashboardComponent } from './playlists-dashboard/playlists-dashboard.component';
import { PlaylistComponent } from './playlist/playlist.component';
import { UserComponent } from './user/user.component';
import { AuthGuard } from './auth/auth.guard';
import { UsersComponent } from './users/users.component';
import { NotFoundComponent } from './not-found/not-found.component';
import { PlaylistDetailsComponent } from './playlist-details/playlist-details.component';
import { AdminComponent } from './admin/admin.component';
import { PlayerComponent } from './playlist-details/player/player.component';

const routes: Routes = [
{ path: '', redirectTo: '/playlists-dashboard', pathMatch: 'full' },
{ path: 'playlists-dashboard', component: PlaylistsDashboardComponent },
{ path: 'login', component: LoginComponent},
{ path: 'registration', component: RegistrationComponent},
{ path: 'not-found', component: NotFoundComponent},
{ path: 'playlist-details/:playlistId' , component: PlaylistDetailsComponent},
{ path: 'player' , component: PlayerComponent},
{ path: 'playlist', component: PlaylistComponent},
{ path: 'user', component: UserComponent},
{ path: 'users', component: UsersComponent},
{ path: 'admin', component: AdminComponent},
{ path: '**', redirectTo: '/not-found'}
// { path: 'admin', component: AdminComponent,canActivate: [AuthGuard]},
// { path: 'playlist', component: PlaylistComponent,canActivate: [AuthGuard]},
// { path: 'user', component: UserComponent, canActivate: [AuthGuard]},
// { path: 'users', component: UsersComponent, canActivate: [AuthGuard]}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
