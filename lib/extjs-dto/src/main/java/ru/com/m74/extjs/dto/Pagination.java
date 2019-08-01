package ru.com.m74.extjs.dto;

/**
 * Пагинация
 *
 * @author mixam
 * @since 24.03.17 20:38
 */
public class Pagination {
    private Integer start;
    private Integer limit;
    private Integer page;

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

    public boolean isValid() {
        return start != null && limit != null && page != null;
    }
}
