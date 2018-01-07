import {Component, Input, OnInit} from '@angular/core';
import {Notification} from "../notification.model";
import {DateService} from "../date.service";

@Component({
  selector: 'app-notification',
  templateUrl: './notification.component.html',
  styleUrls: ['./notification.component.scss']
})
export class NotificationComponent implements OnInit {

  @Input()
  notification: Notification;

  constructor(private dateService: DateService) { }

  ngOnInit() {
  }

  calculateTimeSinceNotificationPoppedUp(notification: Notification) {
    return this.dateService.calculateDifference(this.dateService.now(), this.notification.date);
  }
}
