package com.urantia.teams.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.Document;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A Orders.
 */
@Document(collection = "orders")
public class Orders implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Size(max = 255)
    @Field("order_number")
    private String order_number;

    @Field("cust_id")
    private Integer cust_id;

    @Field("status")
    private String status;

    @Field("created_on")
    private LocalDate created_on;

    @Field("customer_id")
    private String customer_id;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrder_number() {
        return order_number;
    }

    public Orders order_number(String order_number) {
        this.order_number = order_number;
        return this;
    }

    public void setOrder_number(String order_number) {
        this.order_number = order_number;
    }

    public Integer getCust_id() {
        return cust_id;
    }

    public Orders cust_id(Integer cust_id) {
        this.cust_id = cust_id;
        return this;
    }

    public void setCust_id(Integer cust_id) {
        this.cust_id = cust_id;
    }

    public String getStatus() {
        return status;
    }

    public Orders status(String status) {
        this.status = status;
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDate getCreated_on() {
        return created_on;
    }

    public Orders created_on(LocalDate created_on) {
        this.created_on = created_on;
        return this;
    }

    public void setCreated_on(LocalDate created_on) {
        this.created_on = created_on;
    }

    public String getCustomer_id() {
        return customer_id;
    }

    public Orders customer_id(String customer_id) {
        this.customer_id = customer_id;
        return this;
    }

    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Orders orders = (Orders) o;
        if (orders.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), orders.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Orders{" +
            "id=" + getId() +
            ", order_number='" + getOrder_number() + "'" +
            ", cust_id=" + getCust_id() +
            ", status='" + getStatus() + "'" +
            ", created_on='" + getCreated_on() + "'" +
            ", customer_id='" + getCustomer_id() + "'" +
            "}";
    }
}
