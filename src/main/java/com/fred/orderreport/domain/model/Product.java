package com.fred.orderreport.domain.model;

/**
 * Product domain model.
 */
public class Product {

    private String id;

    private String name;

    private String category;

    private double price;

    private double weight;

    private boolean taxable;

    public Product() {
    }

    public Product(String id,
                   String name,
                   String category,
                   double price,
                   double weight,
                   boolean taxable) {

        this.id = id;
        this.name = name;
        this.category = category;
        this.price = price;
        this.weight = weight;
        this.taxable = taxable;
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public boolean isTaxable() {
        return taxable;
    }

    public void setTaxable(boolean taxable) {
        this.taxable = taxable;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", category='" + category + '\'' +
                ", price=" + price +
                ", weight=" + weight +
                ", taxable=" + taxable +
                '}';
    }
}