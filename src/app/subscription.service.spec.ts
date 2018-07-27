import {async, inject, TestBed} from '@angular/core/testing';

import {SubscriptionService} from './subscription.service';
import {HttpClientTestingModule, HttpTestingController} from "@angular/common/http/testing";
import {HttpClientModule} from "@angular/common/http";
import {Subscription} from "./subscription.model";

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

    it("should call the webservice when creating a subscription",
        async(
            inject([SubscriptionService, HttpTestingController], (subscriptionService: SubscriptionService, backend: HttpTestingController) => {
                let subscription: Subscription = new Subscription();
                subscriptionService.create(subscription).subscribe();

                backend.expectOne({
                    url: '/api/subscription/',
                    method: 'POST',
                });
            })
        )
    );

    it("should call the webservice when updating a subscription",
        async(
            inject([SubscriptionService, HttpTestingController], (subscriptionService: SubscriptionService, backend: HttpTestingController) => {
                let subscription: Subscription = new Subscription();
                subscription.id = 100;
                subscriptionService.update(subscription).subscribe();

                backend.expectOne({
                    url: '/api/subscription/100/',
                    method: 'PUT'
                });
            })
        )
    );
});
