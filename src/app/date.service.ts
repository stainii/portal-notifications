import {Injectable} from '@angular/core';
import * as moment from "moment";

@Injectable({
    providedIn: 'root'
})
export class DateService {

  constructor() { }

  now() {
    return moment();
  }

  calculateDifference(date1, date2) {
    let difference = date1.diff(date2);
    return moment.duration(difference).humanize()
  }
}
