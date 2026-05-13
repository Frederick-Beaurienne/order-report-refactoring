package com.fred.orderreport.domain.model;

/**
 * Promotion domain model.
 */
public class Promotion {

    private String code;

    private String type;

    private String value;

    private String active;

    public Promotion() {
    }

    public Promotion(String code,
                     String type,
                     String value,
                     String active) {

        this.code = code;
        this.type = type;
        this.value = value;
        this.active = active;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Promotion{");
        sb.append("code='").append(code).append('\'');
        sb.append(", type='").append(type).append('\'');
        sb.append(", value='").append(value).append('\'');
        sb.append(", active='").append(active).append('\'');
        sb.append('}');
        return sb.toString();
    }
}