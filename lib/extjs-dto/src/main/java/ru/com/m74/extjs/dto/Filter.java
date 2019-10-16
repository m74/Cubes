package ru.com.m74.extjs.dto;

/**
 * @author mixam
 * @since 24.03.17 20:38
 */
public class Filter {
    private String property;
    private Operator operator;
    private Object value;

    public enum Operator {
        like,
        startsWith,
        contains,
        eq,
        gt,
        lt;

        public String sql() {
            switch (this) {
                case like:
                case startsWith:
                case contains:
                    return "like";
                case eq:
                    return "=";
                case lt:
                    return "<";
                case gt:
                    return ">";
            }
            throw new RuntimeException(name());
        }
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public Operator getOperator() {
        return operator;
    }

    public void setOperator(Operator operator) {
        this.operator = operator;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
