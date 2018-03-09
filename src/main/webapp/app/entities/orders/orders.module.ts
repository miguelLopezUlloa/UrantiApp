import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { UranappSharedModule } from '../../shared';
import {
    OrdersService,
    OrdersPopupService,
    OrdersComponent,
    OrdersDetailComponent,
    OrdersDialogComponent,
    OrdersPopupComponent,
    OrdersDeletePopupComponent,
    OrdersDeleteDialogComponent,
    ordersRoute,
    ordersPopupRoute,
    OrdersResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...ordersRoute,
    ...ordersPopupRoute,
];

@NgModule({
    imports: [
        UranappSharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        OrdersComponent,
        OrdersDetailComponent,
        OrdersDialogComponent,
        OrdersDeleteDialogComponent,
        OrdersPopupComponent,
        OrdersDeletePopupComponent,
    ],
    entryComponents: [
        OrdersComponent,
        OrdersDialogComponent,
        OrdersPopupComponent,
        OrdersDeleteDialogComponent,
        OrdersDeletePopupComponent,
    ],
    providers: [
        OrdersService,
        OrdersPopupService,
        OrdersResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class UranappOrdersModule {}
