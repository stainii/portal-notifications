import {async, inject, TestBed} from '@angular/core/testing';
import {HttpClientModule} from '@angular/common/http';
import {HttpClientTestingModule, HttpTestingController} from '@angular/common/http/testing';

import {NotificationService} from './notification.service';

describe('NotificationService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [NotificationService],
      imports: [
        HttpClientModule,
        HttpClientTestingModule
      ]
    });
  });

  it('should be created', inject([NotificationService], (service: NotificationService) => {
    expect(service).toBeTruthy();
  }));

  it("should call the webservice when finding all notifications",
    async(
      inject([NotificationService, HttpTestingController], (notificationService: NotificationService, backend: HttpTestingController) => {

        notificationService.findActiveNotifications().subscribe();

        backend.expectOne({
          url: '/api/notification/?onlyUnread=true',
          method: 'GET'
        });
      })
    )
  );

    it("should call the webservice when marking a notification as read",
        async(
            inject([NotificationService, HttpTestingController], (notificationService: NotificationService, backend: HttpTestingController) => {

                notificationService.markAsRead(100).subscribe();

                backend.expectOne({
                    url: '/api/notification/100/read/',
                    method: 'PUT'
                });
            })
        )
    );
});
