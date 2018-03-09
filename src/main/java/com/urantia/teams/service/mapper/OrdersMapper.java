package com.urantia.teams.service.mapper;

import com.urantia.teams.domain.*;
import com.urantia.teams.service.dto.OrdersDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Orders and its DTO OrdersDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface OrdersMapper extends EntityMapper<OrdersDTO, Orders> {


}
