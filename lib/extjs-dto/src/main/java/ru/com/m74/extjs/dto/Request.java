package ru.com.m74.extjs.dto;

import java.util.Map;

/**
 * Стандартные параметры запроса для Ext.data.Store
 *
 * @author mixam
 * @since 24.03.17 20:38
 */
public class Request {
    private String query;
    private Filter filter[];
    private Sorter sort[];
    private Integer start;
    private Integer limit;
    private Integer page;

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public Filter[] getFilter() {
        return filter;
    }

    public void setFilter(Filter[] filter) {
        this.filter = filter;
    }

    public Sorter[] getSort() {
        return sort;
    }

    public void setSort(Sorter[] sort) {
        this.sort = sort;
    }

    public Integer getStart() {
        return start;
    }

    public void setStart(Integer start) {
        this.start = start;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public boolean isPaging() {
        return start != null && limit != null && page != null;
    }

    public void applyParams(Map<String, Object> params) {
        params.put("query", query);
        params.put("start", start);
        params.put("limit", limit);
    }

}
