package com.urantia.teams.repository;

import com.urantia.teams.domain.Orders;
import org.springframework.stereotype.Repository;

import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Spring Data MongoDB repository for the Orders entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OrdersRepository extends MongoRepository<Orders, String> {

}
