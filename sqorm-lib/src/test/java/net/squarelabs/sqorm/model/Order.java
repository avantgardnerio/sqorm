package net.squarelabs.sqorm.model;

import net.squarelabs.sqorm.annotation.Column;
import net.squarelabs.sqorm.annotation.Table;

@Table(name = "Orders")
public class Order {

    private int orderId;
    private int customerId;
    private int version;

    public Order() {

    }

    public Order(int orderId, int customerId) {
        this.orderId = orderId;
        this.customerId = customerId;
    }

    @Column(name="OrderId", pkOrdinal = 0)
    public int getOrderId() {
        return orderId;
    }

    @Column(name="OrderId", pkOrdinal = 0)
    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    @Column(name="customer_id")
    public int getCustomerId() {
        return customerId;
    }

    @Column(name="customer_id")
    public void setCustomerId(int customerId) {
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
