import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {SubscriptionDetailsComponent} from './subscription-details.component';
import {FormsModule} from "@angular/forms";

describe('SubscriptionDetailsComponent', () => {
    let component: SubscriptionDetailsComponent;
    let fixture: ComponentFixture<SubscriptionDetailsComponent>;

    beforeEach(async(() => {
        TestBed.configureTestingModule({
            declarations: [SubscriptionDetailsComponent],
            imports: [FormsModule]
        })
            .compileComponents();
    }));

    beforeEach(() => {
        fixture = TestBed.createComponent(SubscriptionDetailsComponent);
        component = fixture.componentInstance;
        fixture.detectChanges();
    });

    it('should create', () => {
        expect(component).toBeTruthy();
    });
});
