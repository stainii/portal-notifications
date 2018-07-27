import {Component, EventEmitter, Input, Output} from '@angular/core';
import {Subscription} from "../subscription.model";

@Component({
    selector: 'app-subscription-details',
    templateUrl: './subscription-details.component.html',
    styleUrls: ['./subscription-details.component.scss']
})
export class SubscriptionDetailsComponent {

    @Input()
    subscription: Subscription;

    @Output()
    onSave: EventEmitter<Subscription> = new EventEmitter<Subscription>();

    constructor() {
    }

    save() {
        this.onSave.emit(this.subscription);
    }
}
