import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { LoginComponentComponent } from './login-component/login-component.component';
import { RegistrationComponentComponent } from './registration-component/registration-component.component';
import { AppComponent } from './app.component';
import { DashboardComponent } from './dashboard/dashboard.component';

const routes: Routes = [
{ path: '', redirectTo: '/dashboard', pathMatch: 'full' },
{ path: 'dashboard', component: DashboardComponent },
{ path: 'app-login-component', component: LoginComponentComponent},
{ path: 'app-registration-component', component: RegistrationComponentComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
