import {NotificationAction} from "./notification-action.model";

export interface Notification {

  id: number,

  /** The module that has pushed the notification **/
  origin: string,

  /** Date and that the notification has been pushed **/
  date: Date,

  title: string,
  message: string,

  action: NotificationAction

}
