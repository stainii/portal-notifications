import {inject, TestBed} from '@angular/core/testing';

import {DateService} from './date.service';
import * as moment from "moment";

describe('DateService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [DateService]
    });
  });

  it('should be created', inject([DateService], (service: DateService) => {
    expect(service).toBeTruthy();
  }));

  it('now() should return a date', inject([DateService], (service: DateService) => {
    expect(service.now()).toBeTruthy();
  }));

  it('calculateDifference() should return te difference', inject([DateService], (service: DateService) => {
    let date1 = moment("2013-02-08 09:30");
    let date2 = moment("2013-02-08 08:50");
    let result = service.calculateDifference(date1, date2);
    expect(result).toEqual("40 minutes");
  }));


});
