import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';
import {HttpClientModule} from '@angular/common/http';


import {AppComponent} from './app.component';
import {NotificationService} from "./notification.service";
import {NotificationComponent} from './notification/notification.component';
import {DateService} from "./date.service";


@NgModule({
  declarations: [
    AppComponent,
    NotificationComponent
  ],
  imports: [
    BrowserModule,
    HttpClientModule
  ],
  providers: [
    NotificationService,
    DateService
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
