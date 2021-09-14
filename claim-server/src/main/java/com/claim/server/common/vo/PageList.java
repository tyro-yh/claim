package com.claim.server.common.vo;

import java.util.List;

public class PageList<T> {

    private Long total;

    private List<T> pages;

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public List<T> getPages() {
        return pages;
    }

    public void setPages(List<T> pages) {
        this.pages = pages;
    }
}
