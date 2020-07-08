package ru.com.m74.extjs.dto;

/**
 * @author mixam
 * @since 24.03.17 20:38
 */
public class Filter {
    private String property;
    private String operator;
    private Object value;

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public String getOperator() {
        return operator != null ? operator : "eq";
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
