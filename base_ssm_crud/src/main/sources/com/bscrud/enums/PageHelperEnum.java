package com.bscrud.enums;

public enum PageHelperEnum {
    //分页所需要的页面大小以及所需展示的页面数
    PAGESIZE1(5), PAGESIZE2(10), PAGESIZE(20);

    private int pageSize;

    PageHelperEnum(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPageSize() {
        return pageSize;
    }
}
