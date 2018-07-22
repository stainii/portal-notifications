import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Notification} from "./notification.model";

@Injectable()
export class NotificationService {

  constructor(private _http: HttpClient) { }

  findActiveNotifications() {
    return this._http.get<Notification[]>("/api/notification/?onlyUnread=true");
  }

}
