package com.fred.orderreport.domain.model;

/**
 * Shipping zone domain model.
 */
public class ShippingZone {

    private double base;

    private double perKg;

    public ShippingZone() {
    }

    public ShippingZone(double base, double perKg) {
        this.base = base;
        this.perKg = perKg;
    }

    public double getBase() {
        return base;
    }

    public void setBase(double base) {
        this.base = base;
    }

    public double getPerKg() {
        return perKg;
    }

    public void setPerKg(double perKg) {
        this.perKg = perKg;
    }

    @Override
    public String toString() {
        return "ShippingZone{" +
                "base=" + base +
                ", perKg=" + perKg +
                '}';
    }
}