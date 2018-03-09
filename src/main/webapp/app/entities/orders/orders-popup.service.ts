import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { HttpResponse } from '@angular/common/http';
import { Orders } from './orders.model';
import { OrdersService } from './orders.service';

@Injectable()
export class OrdersPopupService {
    private ngbModalRef: NgbModalRef;

    constructor(
        private modalService: NgbModal,
        private router: Router,
        private ordersService: OrdersService

    ) {
        this.ngbModalRef = null;
    }

    open(component: Component, id?: number | any): Promise<NgbModalRef> {
        return new Promise<NgbModalRef>((resolve, reject) => {
            const isOpen = this.ngbModalRef !== null;
            if (isOpen) {
                resolve(this.ngbModalRef);
            }

            if (id) {
                this.ordersService.find(id)
                    .subscribe((ordersResponse: HttpResponse<Orders>) => {
                        const orders: Orders = ordersResponse.body;
                        if (orders.created_on) {
                            orders.created_on = {
                                year: orders.created_on.getFullYear(),
                                month: orders.created_on.getMonth() + 1,
                                day: orders.created_on.getDate()
                            };
                        }
                        this.ngbModalRef = this.ordersModalRef(component, orders);
                        resolve(this.ngbModalRef);
                    });
            } else {
                // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
                setTimeout(() => {
                    this.ngbModalRef = this.ordersModalRef(component, new Orders());
                    resolve(this.ngbModalRef);
                }, 0);
            }
        });
    }

    ordersModalRef(component: Component, orders: Orders): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.orders = orders;
        modalRef.result.then((result) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true, queryParamsHandling: 'merge' });
            this.ngbModalRef = null;
        }, (reason) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true, queryParamsHandling: 'merge' });
            this.ngbModalRef = null;
        });
        return modalRef;
    }
}
