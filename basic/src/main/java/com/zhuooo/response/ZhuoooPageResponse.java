package com.zhuooo.response;

import java.util.Collection;

public class ZhuoooPageResponse<T> extends ZhuoooResponse<T> {

    private int total = 0;

    private int pageId = 1;

    private int pageSize = 10;

    private boolean hasNext = false;

    /**
     * 计算有没有后续信息
     */
    private void execute() {
        if (data instanceof Collection) {
            int count = (pageId - 1) * pageSize + ((Collection) data).size();
            if (total > count) {
                hasNext = true;
            }
        }
    }

    @Override
    public void setData(T data) {
        super.data = data;
        execute();
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
        execute();
    }

    public int getPageId() {
        return pageId;
    }

    public void setPageId(int pageId) {
        this.pageId = pageId;
        execute();
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
        execute();
    }
}
