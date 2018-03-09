import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Orders } from './orders.model';
import { OrdersPopupService } from './orders-popup.service';
import { OrdersService } from './orders.service';

@Component({
    selector: 'jhi-orders-dialog',
    templateUrl: './orders-dialog.component.html'
})
export class OrdersDialogComponent implements OnInit {

    orders: Orders;
    isSaving: boolean;
    created_onDp: any;

    constructor(
        public activeModal: NgbActiveModal,
        private ordersService: OrdersService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.orders.id !== undefined) {
            this.subscribeToSaveResponse(
                this.ordersService.update(this.orders));
        } else {
            this.subscribeToSaveResponse(
                this.ordersService.create(this.orders));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<Orders>>) {
        result.subscribe((res: HttpResponse<Orders>) =>
            this.onSaveSuccess(res.body), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess(result: Orders) {
        this.eventManager.broadcast({ name: 'ordersListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }
}

@Component({
    selector: 'jhi-orders-popup',
    template: ''
})
export class OrdersPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private ordersPopupService: OrdersPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.ordersPopupService
                    .open(OrdersDialogComponent as Component, params['id']);
            } else {
                this.ordersPopupService
                    .open(OrdersDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
