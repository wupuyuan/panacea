package com.zhuooo.request;

public class ZhuoooPageRequest {
    private int pageId;

    private int pageSize;

    public int getPageId() {
        return pageId;
    }

    public void setPageId(int pageId) {
        this.pageId = pageId;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getLimitStart() {
        if (pageId < 1) {
            pageId = 1;
        }

        if (pageSize < 10) {
            pageSize = 10;
        }

        int ret = (pageId - 1) * pageSize;
        return ret;
    }

    public int getLimitEnd() {
        if (pageId < 1) {
            pageId = 1;
        }

        if (pageSize < 10) {
            pageSize = 10;
        }

        int ret = pageId * pageSize;
        return ret;
    }
}
