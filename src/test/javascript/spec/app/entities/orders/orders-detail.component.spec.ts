/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { UranappTestModule } from '../../../test.module';
import { OrdersDetailComponent } from '../../../../../../main/webapp/app/entities/orders/orders-detail.component';
import { OrdersService } from '../../../../../../main/webapp/app/entities/orders/orders.service';
import { Orders } from '../../../../../../main/webapp/app/entities/orders/orders.model';

describe('Component Tests', () => {

    describe('Orders Management Detail Component', () => {
        let comp: OrdersDetailComponent;
        let fixture: ComponentFixture<OrdersDetailComponent>;
        let service: OrdersService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [UranappTestModule],
                declarations: [OrdersDetailComponent],
                providers: [
                    OrdersService
                ]
            })
            .overrideTemplate(OrdersDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(OrdersDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(OrdersService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new HttpResponse({
                    body: new Orders('123')
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith('123');
                expect(comp.orders).toEqual(jasmine.objectContaining({id: '123'}));
            });
        });
    });

});
