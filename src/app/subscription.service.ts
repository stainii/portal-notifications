import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Subscription} from "./subscription.model";

@Injectable({
    providedIn: 'root'
})
export class SubscriptionService {

    constructor(private _http: HttpClient) {
    }

    findAll() {
        return this._http.get<Subscription[]>("/api/subscription/");
    }

}
