import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { JhiDateUtils } from 'ng-jhipster';

import { Orders } from './orders.model';
import { createRequestOption } from '../../shared';

export type EntityResponseType = HttpResponse<Orders>;

@Injectable()
export class OrdersService {

    private resourceUrl =  SERVER_API_URL + 'api/orders';

    constructor(private http: HttpClient, private dateUtils: JhiDateUtils) { }

    create(orders: Orders): Observable<EntityResponseType> {
        const copy = this.convert(orders);
        return this.http.post<Orders>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    update(orders: Orders): Observable<EntityResponseType> {
        const copy = this.convert(orders);
        return this.http.put<Orders>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    find(id: string): Observable<EntityResponseType> {
        return this.http.get<Orders>(`${this.resourceUrl}/${id}`, { observe: 'response'})
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    query(req?: any): Observable<HttpResponse<Orders[]>> {
        const options = createRequestOption(req);
        return this.http.get<Orders[]>(this.resourceUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<Orders[]>) => this.convertArrayResponse(res));
    }

    delete(id: string): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response'});
    }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: Orders = this.convertItemFromServer(res.body);
        return res.clone({body});
    }

    private convertArrayResponse(res: HttpResponse<Orders[]>): HttpResponse<Orders[]> {
        const jsonResponse: Orders[] = res.body;
        const body: Orders[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({body});
    }

    /**
     * Convert a returned JSON object to Orders.
     */
    private convertItemFromServer(orders: Orders): Orders {
        const copy: Orders = Object.assign({}, orders);
        copy.created_on = this.dateUtils
            .convertLocalDateFromServer(orders.created_on);
        return copy;
    }

    /**
     * Convert a Orders to a JSON which can be sent to the server.
     */
    private convert(orders: Orders): Orders {
        const copy: Orders = Object.assign({}, orders);
        copy.created_on = this.dateUtils
            .convertLocalDateToServer(orders.created_on);
        return copy;
    }
}
