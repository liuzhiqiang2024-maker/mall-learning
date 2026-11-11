package com.example.malldemo.dto;

import java.util.List;
public class PageResult<T> {
    private List<T> records;
    private Long total;
    private Long pages;

    // 泛型T，代表records里面放什么类型的数据
    public static <T> PageResult<T> build(List<T> records, Long total, Integer pageSize) {
        PageResult<T> pageResult = new PageResult<>();
        pageResult.setRecords(records);
        pageResult.setTotal(total);

        // 先声明变量；
        Long pages;
        if (total == 0) {
            pages = 0L;
        } else {
            pages = total % pageSize == 0 ? total / pageSize : total / pageSize + 1;
        }
        pageResult.setPages(pages);
        return pageResult;
    }
    //getter、setter
    public List<T> getRecords() {
        return records;
    }
    public void setRecords(List<T> records) {
        this.records = records;
    }
    public Long getTotal() {
        return total;
    }
    public void setTotal(Long total) {
        this.total = total;
    }
    public Long getPages() {
        return pages;
    }
    public void setPages(Long pages) {
        this.pages = pages;
    }
}


