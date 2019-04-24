import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { LoginComponent } from './login/login.component';
import { RegistrationComponent } from './registration/registration.component';
import { DashboardComponent } from './dashboard/dashboard.component';
import { PlaylistComponent } from './playlist/playlist.component';
import { UserComponent } from './user/user.component';
import { AuthGuard } from './auth/auth.guard';

const routes: Routes = [
{ path: '', redirectTo: '/dashboard', pathMatch: 'full' },
{ path: 'dashboard', component: DashboardComponent },
{ path: 'login-component', component: LoginComponent},
{ path: 'registration', component: RegistrationComponent},
{ path: 'playlist-component', component: PlaylistComponent,canActivate: [AuthGuard]},
{ path: 'user-component', component: UserComponent, canActivate: [AuthGuard]}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
