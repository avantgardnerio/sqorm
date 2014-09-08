package net.squarelabs.sqorm;

import net.squarelabs.sqorm.annotation.Column;
import net.squarelabs.sqorm.annotation.Table;

@Table(name = "customer")
public class Customer {
    private String name;
    private int customerId;

    public Customer(int id, String name) {
        this.customerId = id;
        this.name = name;
    }

    @Column(name="customer_id", pkOrdinal = 1)
    public int getCustomerId() {
        return customerId;
    }

    @Column(name="customer_id", pkOrdinal = 1)
    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    @Column(name = "name")
    public String getName() {
        return name;
    }

    @Column(name = "name")
    public void setName(String name) {
        this.name = name;
    }

}
