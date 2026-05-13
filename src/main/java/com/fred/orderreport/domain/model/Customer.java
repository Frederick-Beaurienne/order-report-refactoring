package com.fred.orderreport.domain.model;

/**
 * Customer domain model.
 */
public class Customer {

    private String id;

    private String name;

    private String level;

    private String shippingZone;

    private String currency;

    public Customer() {
    }

    public Customer(String id,
                    String name,
                    String level,
                    String shippingZone,
                    String currency) {

        this.id = id;
        this.name = name;
        this.level = level;
        this.shippingZone = shippingZone;
        this.currency = currency;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getShippingZone() {
        return shippingZone;
    }

    public void setShippingZone(String shippingZone) {
        this.shippingZone = shippingZone;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Customer{");
        sb.append("id='").append(id).append('\'');
        sb.append(", name='").append(name).append('\'');
        sb.append(", level='").append(level).append('\'');
        sb.append(", shippingZone='").append(shippingZone).append('\'');
        sb.append(", currency='").append(currency).append('\'');
        sb.append('}');
        return sb.toString();
    }
}