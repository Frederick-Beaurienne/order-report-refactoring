package com.fred.orderreport.domain.model;

/**
 * Order domain model.
 */
public class Order {

    private String id;

    private String customerId;

    private String productId;

    private int quantity;

    private double unitPrice;

    private String date;

    private String promoCode;

    private String time;

    public Order() {
    }

    public Order(String id,
                 String customerId,
                 String productId,
                 int quantity,
                 double unitPrice,
                 String date,
                 String promoCode,
                 String time) {

        this.id = id;
        this.customerId = customerId;
        this.productId = productId;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.date = date;
        this.promoCode = promoCode;
        this.time = time;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPromoCode() {
        return promoCode;
    }

    public void setPromoCode(String promoCode) {
        this.promoCode = promoCode;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Order{");
        sb.append("id='").append(id).append('\'');
        sb.append(", customerId='").append(customerId).append('\'');
        sb.append(", productId='").append(productId).append('\'');
        sb.append(", quantity=").append(quantity);
        sb.append(", unitPrice=").append(unitPrice);
        sb.append(", date='").append(date).append('\'');
        sb.append(", promoCode='").append(promoCode).append('\'');
        sb.append(", time='").append(time).append('\'');
        sb.append('}');
        return sb.toString();
    }
}