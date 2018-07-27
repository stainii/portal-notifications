import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {Subscription} from "../subscription.model";

@Component({
    selector: 'app-subscription-list',
    templateUrl: './subscription-list.component.html',
    styleUrls: ['./subscription-list.component.scss']
})
export class SubscriptionListComponent implements OnInit {

    @Input()
    set subscriptions(subscriptions: Subscription[]) {
        this._subscriptions = subscriptions;
        if (this.subscriptions && this.subscriptions.length > 0
            && !this.selectedSubscription) {
            this.selectSubscription(this.subscriptions[0]);
        }
    };
    get subscriptions() {
        return this._subscriptions;
    }

    @Output()
    subscriptionSelected: EventEmitter<Subscription> = new EventEmitter<Subscription>();

    selectedSubscription: Subscription;
    private _subscriptions: Subscription[];

    ngOnInit(): void {

    }

    selectSubscription(subscription: Subscription) {
        this.selectedSubscription = subscription;
        this.subscriptionSelected.emit(subscription);
    }

    createNewSubscription() {
        let newSubscription = new Subscription();
        this.subscriptions.push(newSubscription);
        this.selectedSubscription = newSubscription;
        this.subscriptionSelected.emit(newSubscription);
    }


}
