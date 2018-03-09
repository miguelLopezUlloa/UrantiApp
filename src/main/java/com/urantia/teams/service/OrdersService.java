package com.urantia.teams.service;

import com.urantia.teams.service.dto.OrdersDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing Orders.
 */
public interface OrdersService {

    /**
     * Save a orders.
     *
     * @param ordersDTO the entity to save
     * @return the persisted entity
     */
    OrdersDTO save(OrdersDTO ordersDTO);

    /**
     * Get all the orders.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<OrdersDTO> findAll(Pageable pageable);

    /**
     * Get the "id" orders.
     *
     * @param id the id of the entity
     * @return the entity
     */
    OrdersDTO findOne(String id);

    /**
     * Delete the "id" orders.
     *
     * @param id the id of the entity
     */
    void delete(String id);
}
