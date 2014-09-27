package net.squarelabs.sqorm.model;

import net.squarelabs.sqorm.annotation.Column;
import net.squarelabs.sqorm.annotation.Table;

import java.util.UUID;

@Table(name = "Orders")
public class Order {

    private int orderId;
    private UUID customerId;
    private int version;

    public Order() {

    }

    public Order(UUID customerId) {
        this.customerId = customerId;
    }

    @Column(name="order_id", autoIncrement = true, pkOrdinal = 0)
    public int getOrderId() {
        return orderId;
    }

    @Column(name="order_id", autoIncrement = true, pkOrdinal = 0)
    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    @Column(name="customer_id")
    public UUID getCustomerId() {
        return customerId;
    }

    @Column(name="customer_id")
    public void setCustomerId(UUID customerId) {
        this.customerId = customerId;
    }

    @Column(name = "version", isVersion = true)
    public int getVersion() {
        return version;
    }

    @Column(name = "version", isVersion = true)
    public void setVersion(int version) {
        this.version = version;
    }

}
