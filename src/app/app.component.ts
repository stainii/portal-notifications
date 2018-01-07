import {Component} from '@angular/core';
import {NotificationService} from "./notification.service";
import {Notification} from "./notification.model";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  notifications: Notification[];

  constructor(private _notificationService: NotificationService) {
    this._notificationService.findActiveNotifications().subscribe(notifications => {
      this.notifications = notifications;
      console.log(this.notifications);
    })
  }
}
