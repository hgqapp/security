package com.hgq.security.config;

/**
 * @author houguangqiang
 * @date 2019-01-25
 * @since 1.0
 */
public class PageParam {

    private int page = 1;
    private int size = 20;

    public PageParam() {
    }

    public PageParam(int page, int size) {
        setPage(page);
        setSize(size);
    }

    public void setPage(int page) {
        if (page > 0) {
            this.page = page;
        }
    }

    public void setSize(int size) {
        if (size > 0) {
            this.size = size;
        }
    }

    public int page() {
        return page - 1;
    }

    public int size() {
        return size;
    }
}
