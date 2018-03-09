import { BaseEntity } from './../../shared';

export class Orders implements BaseEntity {
    constructor(
        public id?: string,
        public order_number?: string,
        public cust_id?: number,
        public status?: string,
        public created_on?: any,
        public customer_id?: string,
    ) {
    }
}
