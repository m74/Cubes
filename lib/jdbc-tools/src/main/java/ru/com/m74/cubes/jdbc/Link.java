package ru.com.m74.cubes.jdbc;

public class Link {
    private Object id;
    private String title;
    private String businessKey;

    public Link(Object id, String title) {
        this.id = id;
        this.title = title;
    }

    public Object getId() {
        return id;
    }

    public void setId(Object id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBusinessKey() {
        return businessKey;
    }

    public void setBusinessKey(String businessKey) {
        this.businessKey = businessKey;
    }
}
