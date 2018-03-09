/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { UranappTestModule } from '../../../test.module';
import { OrdersComponent } from '../../../../../../main/webapp/app/entities/orders/orders.component';
import { OrdersService } from '../../../../../../main/webapp/app/entities/orders/orders.service';
import { Orders } from '../../../../../../main/webapp/app/entities/orders/orders.model';

describe('Component Tests', () => {

    describe('Orders Management Component', () => {
        let comp: OrdersComponent;
        let fixture: ComponentFixture<OrdersComponent>;
        let service: OrdersService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [UranappTestModule],
                declarations: [OrdersComponent],
                providers: [
                    OrdersService
                ]
            })
            .overrideTemplate(OrdersComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(OrdersComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(OrdersService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new HttpHeaders().append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of(new HttpResponse({
                    body: [new Orders('123')],
                    headers
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.orders[0]).toEqual(jasmine.objectContaining({id: '123'}));
            });
        });
    });

});
