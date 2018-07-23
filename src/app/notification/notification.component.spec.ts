import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {NotificationComponent} from './notification.component';
import {DateService} from "../date.service";
import {NotificationService} from "../notification.service";

describe('NotificationComponent', () => {
    let component: NotificationComponent;
    let fixture: ComponentFixture<NotificationComponent>;

    beforeEach(async(() => {
        let stubNotificationService: NotificationService = new NotificationService(null);
        TestBed.configureTestingModule({
            declarations: [NotificationComponent],
            providers: [DateService, {provide: NotificationService, use: stubNotificationService}]
        })
            .compileComponents();
    }));

    beforeEach(() => {
        fixture = TestBed.createComponent(NotificationComponent);
        component = fixture.componentInstance;
        fixture.detectChanges();
    });

    it('should create', () => {
        expect(component).toBeTruthy();
    });
});
