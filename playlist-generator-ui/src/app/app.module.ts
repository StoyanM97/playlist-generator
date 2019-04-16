import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { BingmapServiceDashboardComponent } from './bingmap-service-dashboard/bingmap-service-dashboard.component';

@NgModule({
  declarations: [
    AppComponent,
    BingmapServiceDashboardComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
