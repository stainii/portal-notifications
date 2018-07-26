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

    constructor(private _subscriptionService: SubscriptionService) {
    }

    ngOnInit() {
        this._subscriptionService.findAll().subscribe(subscriptions => {
            this.subscriptions = subscriptions;
        })
    }

}
