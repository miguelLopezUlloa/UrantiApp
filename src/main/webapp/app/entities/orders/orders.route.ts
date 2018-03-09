import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil } from 'ng-jhipster';

import { UserRouteAccessService } from '../../shared';
import { OrdersComponent } from './orders.component';
import { OrdersDetailComponent } from './orders-detail.component';
import { OrdersPopupComponent } from './orders-dialog.component';
import { OrdersDeletePopupComponent } from './orders-delete-dialog.component';

@Injectable()
export class OrdersResolvePagingParams implements Resolve<any> {

    constructor(private paginationUtil: JhiPaginationUtil) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const page = route.queryParams['page'] ? route.queryParams['page'] : '1';
        const sort = route.queryParams['sort'] ? route.queryParams['sort'] : 'id,asc';
        return {
            page: this.paginationUtil.parsePage(page),
            predicate: this.paginationUtil.parsePredicate(sort),
            ascending: this.paginationUtil.parseAscending(sort)
      };
    }
}

export const ordersRoute: Routes = [
    {
        path: 'orders',
        component: OrdersComponent,
        resolve: {
            'pagingParams': OrdersResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'uranappApp.orders.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'orders/:id',
        component: OrdersDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'uranappApp.orders.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const ordersPopupRoute: Routes = [
    {
        path: 'orders-new',
        component: OrdersPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'uranappApp.orders.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'orders/:id/edit',
        component: OrdersPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'uranappApp.orders.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'orders/:id/delete',
        component: OrdersDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'uranappApp.orders.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
