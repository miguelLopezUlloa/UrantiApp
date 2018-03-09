import { BaseEntity } from './../../shared';

export class Product implements BaseEntity {
    constructor(
        public id?: string,
        public name?: string,
        public description?: string,
        public price?: number,
    ) {
    }
}
