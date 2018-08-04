import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {Notification} from "../notification.model";
import {DateService} from "../date.service";
import {NotificationService} from "../notification.service";

@Component({
    selector: 'app-notification',
    templateUrl: './notification.component.html',
    styleUrls: ['./notification.component.scss']
})
export class NotificationComponent implements OnInit {

    @Input()
    notification: Notification;

    read: boolean = false;

    @Output()
    onRead: EventEmitter<Notification> = new EventEmitter<Notification>();

    constructor(private dateService: DateService,
                private _notificationService: NotificationService) {
    }

    ngOnInit() {
    }

    calculateTimeSinceNotificationPoppedUp(notification: Notification) {
        return this.dateService.calculateDifference(this.dateService.now(), notification.date);
    }

    markAsRead(id: number, $event: Event) {
        $event.stopPropagation();
        this._notificationService.markAsRead(id).subscribe(() => {
            this.read = true;
            this.onRead.emit(this.notification);
        });
    }
}
