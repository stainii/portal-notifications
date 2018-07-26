import {Component} from '@angular/core';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {

    active: string;

    constructor() {
        this.activateNotificationList();
    }

    activateNotificationList(): void {
        this.active = "NOTIFICATION_LIST";
    }

    activateSubscriptionEditor(): void {
        this.active = "SUBSCRIPTION_EDITOR";
    }
}
