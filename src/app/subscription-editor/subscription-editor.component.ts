import {Component, OnInit} from '@angular/core';
import {SubscriptionService} from "../subscription.service";
import {Subscription} from "../subscription.model";

@Component({
    selector: 'app-subscription-editor',
    templateUrl: './subscription-editor.component.html',
    styleUrls: ['./subscription-editor.component.scss']
})
export class SubscriptionEditorComponent implements OnInit {

    subscriptions: Subscription[];
    currentlyEditing: Subscription;

    constructor(private _subscriptionService: SubscriptionService) {
    }

    ngOnInit() {
        this._subscriptionService.findAll().subscribe(subscriptions => {
            if (subscriptions && subscriptions.length > 0) {
                this.subscriptions = subscriptions;
            }
        })
    }

    selectSubscription(subscription: Subscription) {
        this.currentlyEditing = subscription;
    }

    save(subscription: Subscription) {
        if (subscription.id) {
            this._subscriptionService.update(subscription).subscribe();
        } else {
            this._subscriptionService.create(subscription).subscribe(persistedSubscription => {
                subscription.id = persistedSubscription.id;
            });
        }
    }

}
