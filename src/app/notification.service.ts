import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Notification} from "./notification.model";

@Injectable({
    providedIn: 'root'
})
export class NotificationService {

  constructor(private _http: HttpClient) { }

  findActiveNotifications() {
    return this._http.get<Notification[]>("/api/notification/?onlyUnread=true");
  }

  markAsRead(id: number) {
      return this._http.put("/api/notification/" + id + "/read/", {
          id: id,
          read: true
      });
  }

}
