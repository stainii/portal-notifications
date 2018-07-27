import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';
import {HttpClientModule} from '@angular/common/http';
import {FontAwesomeModule} from '@fortawesome/angular-fontawesome';

import {AppComponent} from './app.component';
import {NotificationComponent} from './notification/notification.component';
import {NotificationListComponent} from './notification-list/notification-list.component';
import {SubscriptionEditorComponent} from './subscription-editor/subscription-editor.component';
/* manage font-awesome icons */
import {library} from '@fortawesome/fontawesome-svg-core';
import {faBell, faCog} from '@fortawesome/free-solid-svg-icons';
import {SubscriptionListComponent} from './subscription-list/subscription-list.component';
import {SubscriptionDetailsComponent} from './subscription-details/subscription-details.component';
import {FormsModule} from "@angular/forms";

library.add(faBell);
library.add(faCog);

@NgModule({
    declarations: [
        AppComponent,
        NotificationComponent,
        NotificationListComponent,
        SubscriptionEditorComponent,
        SubscriptionListComponent,
        SubscriptionDetailsComponent
    ],
    imports: [
        BrowserModule,
        HttpClientModule,
        FontAwesomeModule,
        FormsModule
    ],
    bootstrap: [AppComponent]
})
export class AppModule {
}
