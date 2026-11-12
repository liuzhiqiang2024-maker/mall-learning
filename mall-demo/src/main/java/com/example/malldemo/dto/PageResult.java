package com.example.malldemo.dto;
import lombok.Data;
import java.util.List;
@Data
public class PageResult<T> {
    private List<T> records;
    private Long total;
    private Long pages;
    private Integer PageNum;
    private Integer PageSize;

    // 泛型T，代表records里面放什么类型的数据
    public static <T> PageResult<T> build(List<T> records, Long total, Integer pageNum,Integer pageSize) {
        PageResult<T> pageResult = new PageResult<>();
        pageResult.setRecords(records);
        pageResult.setTotal(total);
        pageResult.setPageNum(pageNum);
        pageResult.setPageSize(pageSize);
        // 计算总页数
        long pages = (total + pageSize - 1) / pageSize;
        pageResult.setPages(pages);

        return pageResult;
        // 先声明变量；
    }

}


