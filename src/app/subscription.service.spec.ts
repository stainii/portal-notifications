import {async, inject, TestBed} from '@angular/core/testing';

import {SubscriptionService} from './subscription.service';
import {HttpClientTestingModule, HttpTestingController} from "@angular/common/http/testing";
import {HttpClientModule} from "@angular/common/http";

describe('SubscriptionService', () => {
    beforeEach(() => {
        TestBed.configureTestingModule({
            providers: [SubscriptionService],
            imports: [
                HttpClientModule,
                HttpClientTestingModule
            ]
        });
    });

    it('should be created', inject([SubscriptionService], (service: SubscriptionService) => {
        expect(service).toBeTruthy();
    }));

    it("should call the webservice when finding all subscriptions",
        async(
            inject([SubscriptionService, HttpTestingController], (subscriptionService: SubscriptionService, backend: HttpTestingController) => {

                subscriptionService.findAll().subscribe();

                backend.expectOne({
                    url: '/api/subscription/',
                    method: 'GET'
                });
            })
        )
    );
});
