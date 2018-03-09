package com.urantia.teams.web.rest;

import com.urantia.teams.UranappApp;

import com.urantia.teams.domain.Orders;
import com.urantia.teams.repository.OrdersRepository;
import com.urantia.teams.service.OrdersService;
import com.urantia.teams.service.dto.OrdersDTO;
import com.urantia.teams.service.mapper.OrdersMapper;
import com.urantia.teams.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static com.urantia.teams.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the OrdersResource REST controller.
 *
 * @see OrdersResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = UranappApp.class)
public class OrdersResourceIntTest {

    private static final String DEFAULT_ORDER_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_ORDER_NUMBER = "BBBBBBBBBB";

    private static final Integer DEFAULT_CUST_ID = 1;
    private static final Integer UPDATED_CUST_ID = 2;

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_CREATED_ON = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATED_ON = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_CUSTOMER_ID = "AAAAAAAAAA";
    private static final String UPDATED_CUSTOMER_ID = "BBBBBBBBBB";

    @Autowired
    private OrdersRepository ordersRepository;

    @Autowired
    private OrdersMapper ordersMapper;

    @Autowired
    private OrdersService ordersService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    private MockMvc restOrdersMockMvc;

    private Orders orders;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final OrdersResource ordersResource = new OrdersResource(ordersService);
        this.restOrdersMockMvc = MockMvcBuilders.standaloneSetup(ordersResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Orders createEntity() {
        Orders orders = new Orders()
            .order_number(DEFAULT_ORDER_NUMBER)
            .cust_id(DEFAULT_CUST_ID)
            .status(DEFAULT_STATUS)
            .created_on(DEFAULT_CREATED_ON)
            .customer_id(DEFAULT_CUSTOMER_ID);
        return orders;
    }

    @Before
    public void initTest() {
        ordersRepository.deleteAll();
        orders = createEntity();
    }

    @Test
    public void createOrders() throws Exception {
        int databaseSizeBeforeCreate = ordersRepository.findAll().size();

        // Create the Orders
        OrdersDTO ordersDTO = ordersMapper.toDto(orders);
        restOrdersMockMvc.perform(post("/api/orders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ordersDTO)))
            .andExpect(status().isCreated());

        // Validate the Orders in the database
        List<Orders> ordersList = ordersRepository.findAll();
        assertThat(ordersList).hasSize(databaseSizeBeforeCreate + 1);
        Orders testOrders = ordersList.get(ordersList.size() - 1);
        assertThat(testOrders.getOrder_number()).isEqualTo(DEFAULT_ORDER_NUMBER);
        assertThat(testOrders.getCust_id()).isEqualTo(DEFAULT_CUST_ID);
        assertThat(testOrders.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testOrders.getCreated_on()).isEqualTo(DEFAULT_CREATED_ON);
        assertThat(testOrders.getCustomer_id()).isEqualTo(DEFAULT_CUSTOMER_ID);
    }

    @Test
    public void createOrdersWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = ordersRepository.findAll().size();

        // Create the Orders with an existing ID
        orders.setId("existing_id");
        OrdersDTO ordersDTO = ordersMapper.toDto(orders);

        // An entity with an existing ID cannot be created, so this API call must fail
        restOrdersMockMvc.perform(post("/api/orders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ordersDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Orders in the database
        List<Orders> ordersList = ordersRepository.findAll();
        assertThat(ordersList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    public void getAllOrders() throws Exception {
        // Initialize the database
        ordersRepository.save(orders);

        // Get all the ordersList
        restOrdersMockMvc.perform(get("/api/orders?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(orders.getId())))
            .andExpect(jsonPath("$.[*].order_number").value(hasItem(DEFAULT_ORDER_NUMBER.toString())))
            .andExpect(jsonPath("$.[*].cust_id").value(hasItem(DEFAULT_CUST_ID)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].created_on").value(hasItem(DEFAULT_CREATED_ON.toString())))
            .andExpect(jsonPath("$.[*].customer_id").value(hasItem(DEFAULT_CUSTOMER_ID.toString())));
    }

    @Test
    public void getOrders() throws Exception {
        // Initialize the database
        ordersRepository.save(orders);

        // Get the orders
        restOrdersMockMvc.perform(get("/api/orders/{id}", orders.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(orders.getId()))
            .andExpect(jsonPath("$.order_number").value(DEFAULT_ORDER_NUMBER.toString()))
            .andExpect(jsonPath("$.cust_id").value(DEFAULT_CUST_ID))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.created_on").value(DEFAULT_CREATED_ON.toString()))
            .andExpect(jsonPath("$.customer_id").value(DEFAULT_CUSTOMER_ID.toString()));
    }

    @Test
    public void getNonExistingOrders() throws Exception {
        // Get the orders
        restOrdersMockMvc.perform(get("/api/orders/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    public void updateOrders() throws Exception {
        // Initialize the database
        ordersRepository.save(orders);
        int databaseSizeBeforeUpdate = ordersRepository.findAll().size();

        // Update the orders
        Orders updatedOrders = ordersRepository.findOne(orders.getId());
        updatedOrders
            .order_number(UPDATED_ORDER_NUMBER)
            .cust_id(UPDATED_CUST_ID)
            .status(UPDATED_STATUS)
            .created_on(UPDATED_CREATED_ON)
            .customer_id(UPDATED_CUSTOMER_ID);
        OrdersDTO ordersDTO = ordersMapper.toDto(updatedOrders);

        restOrdersMockMvc.perform(put("/api/orders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ordersDTO)))
            .andExpect(status().isOk());

        // Validate the Orders in the database
        List<Orders> ordersList = ordersRepository.findAll();
        assertThat(ordersList).hasSize(databaseSizeBeforeUpdate);
        Orders testOrders = ordersList.get(ordersList.size() - 1);
        assertThat(testOrders.getOrder_number()).isEqualTo(UPDATED_ORDER_NUMBER);
        assertThat(testOrders.getCust_id()).isEqualTo(UPDATED_CUST_ID);
        assertThat(testOrders.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testOrders.getCreated_on()).isEqualTo(UPDATED_CREATED_ON);
        assertThat(testOrders.getCustomer_id()).isEqualTo(UPDATED_CUSTOMER_ID);
    }

    @Test
    public void updateNonExistingOrders() throws Exception {
        int databaseSizeBeforeUpdate = ordersRepository.findAll().size();

        // Create the Orders
        OrdersDTO ordersDTO = ordersMapper.toDto(orders);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restOrdersMockMvc.perform(put("/api/orders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ordersDTO)))
            .andExpect(status().isCreated());

        // Validate the Orders in the database
        List<Orders> ordersList = ordersRepository.findAll();
        assertThat(ordersList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    public void deleteOrders() throws Exception {
        // Initialize the database
        ordersRepository.save(orders);
        int databaseSizeBeforeDelete = ordersRepository.findAll().size();

        // Get the orders
        restOrdersMockMvc.perform(delete("/api/orders/{id}", orders.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Orders> ordersList = ordersRepository.findAll();
        assertThat(ordersList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Orders.class);
        Orders orders1 = new Orders();
        orders1.setId("id1");
        Orders orders2 = new Orders();
        orders2.setId(orders1.getId());
        assertThat(orders1).isEqualTo(orders2);
        orders2.setId("id2");
        assertThat(orders1).isNotEqualTo(orders2);
        orders1.setId(null);
        assertThat(orders1).isNotEqualTo(orders2);
    }

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(OrdersDTO.class);
        OrdersDTO ordersDTO1 = new OrdersDTO();
        ordersDTO1.setId("id1");
        OrdersDTO ordersDTO2 = new OrdersDTO();
        assertThat(ordersDTO1).isNotEqualTo(ordersDTO2);
        ordersDTO2.setId(ordersDTO1.getId());
        assertThat(ordersDTO1).isEqualTo(ordersDTO2);
        ordersDTO2.setId("id2");
        assertThat(ordersDTO1).isNotEqualTo(ordersDTO2);
        ordersDTO1.setId(null);
        assertThat(ordersDTO1).isNotEqualTo(ordersDTO2);
    }
}
