package ru.com.m74.extjs.dto;

public class RootNode<T extends Node> implements Node<T, Object> {

    private Iterable<T> children;
    private MetaData metaData = new MetaData();

    public RootNode(Iterable<T> children) {
        setChildren(children);
    }

    @Override
    public Object getId() {
        return null;
    }

    @Override
    public Object getParentId() {
        return null;
    }

    @Override
    public void setChildren(Iterable<T> nodes) {
        this.children = nodes;
    }

    @Override
    public Iterable<T> getChildren() {
        return children;
    }

    public MetaData getMetaData() {
        return metaData;
    }

    public void setMetaData(MetaData metaData) {
        this.metaData = metaData;
    }

    class MetaData {
        private final String root = "children";
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
