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

    class MetaData {
        private final String root = "records";
        private final String idProperty = "id";
        private final String id = "id";
        private final String totalProperty = "totalCount";
        private final String successProperty = "success";
        private final String messageProperty = "message";

        public String getRoot() {
            return root;
        }

        public String getIdProperty() {
            return idProperty;
        }

        public String getId() {
            return id;
        }

        public String getTotalProperty() {
            return totalProperty;
        }

        public String getSuccessProperty() {
            return successProperty;
        }

        public String getMessageProperty() {
            return messageProperty;
        }
    }
}
