package ru.com.m74.extjs.dto;

public class Response<T> {
    private T records;
    private Long totalCount;

    private boolean success = true;

    private String message;

    private final MetaData metaData = new MetaData();

    public Response(T records) {
        this.records = records;
    }

    public T getRecords() {
        return records;
    }

    public void setRecords(T records) {
        this.records = records;
    }

    public Long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Long totalCount) {
        this.totalCount = totalCount;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public MetaData getMetaData() {
        return metaData;
    }
}
