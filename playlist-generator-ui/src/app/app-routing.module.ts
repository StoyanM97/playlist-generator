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
import { AdminGuard } from './auth/admin.guard';

const routes: Routes = [
{ path: '', redirectTo: '/playlists-dashboard', pathMatch: 'full' },
{ path: 'playlists-dashboard', component: PlaylistsDashboardComponent },
{ path: 'login', component: LoginComponent},
{ path: 'registration', component: RegistrationComponent},
{ path: 'not-found', component: NotFoundComponent},
{ path: 'playlist-details/:playlistId' , component: PlaylistDetailsComponent},
{ path: 'player' , component: PlayerComponent},
{ path: 'playlist', component: PlaylistComponent, canActivate: [AuthGuard]},
{ path: 'user', component: UserComponent, canActivate: [AuthGuard]},
{ path: 'users', component: UsersComponent, canActivate: [AdminGuard]},
{ path: 'admin', component: AdminComponent, canActivate: [AdminGuard]},
{ path: '**', redirectTo: '/not-found'}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
