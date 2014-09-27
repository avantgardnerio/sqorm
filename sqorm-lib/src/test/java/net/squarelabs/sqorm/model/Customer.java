package net.squarelabs.sqorm.model;

import net.squarelabs.sqorm.annotation.Column;
import net.squarelabs.sqorm.annotation.Table;

import java.util.UUID;

@Table(name = "Customer")
public class Customer {
    private String name;
    private UUID customerId;

    private int version;

    public Customer() {

    }

    public Customer(UUID id, String name) {
        this.customerId = id;
        this.name = name;
    }

    @Column(name="CustomerId", pkOrdinal = 0)
    public UUID getCustomerId() {
        return customerId;
    }

    @Column(name="CustomerId", pkOrdinal = 0)
    public void setCustomerId(UUID customerId) {
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

    @Column(name = "version", isVersion = true)
    public int getVersion() {
        return version;
    }

    @Column(name = "version", isVersion = true)
    public void setVersion(int version) {
        this.version = version;
    }

}
