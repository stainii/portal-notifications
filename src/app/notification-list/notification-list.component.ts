import {Component} from '@angular/core';
import {Notification} from "../notification.model";
import {NotificationService} from "../notification.service";

@Component({
  selector: 'app-notification-list',
  templateUrl: './notification-list.component.html',
  styleUrls: ['./notification-list.component.scss']
})
export class NotificationListComponent {

    notifications: Notification[];

    constructor(private _notificationService: NotificationService) {
        this._notificationService.findActiveNotifications().subscribe(notifications => {
            this.notifications = notifications;
        })
    }

}
