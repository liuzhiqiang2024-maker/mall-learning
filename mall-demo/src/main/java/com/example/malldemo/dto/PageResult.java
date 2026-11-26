package com.example.malldemo.dto;
import lombok.Data;
import java.util.List;
// 分页结果包含本页记录和符合查询条件的总条数。T 是每条记录的类型，例如 Orders。
@Data
public class PageResult<T> {
    private List<T> records;
    private Long total;
    private Long pages;
    private Integer PageNum;
    private Integer PageSize;

    // 静态方法单独声明 <T>，调用时可根据传入的 List<T> 推断记录类型。
    public static <T> PageResult<T> build(List<T> records, Long total, Integer pageNum,Integer pageSize) {
        PageResult<T> pageResult = new PageResult<>();
        pageResult.setRecords(records);
        pageResult.setTotal(total);
        pageResult.setPageNum(pageNum);
        pageResult.setPageSize(pageSize);
        // 整数除法会舍去余数，加上 pageSize - 1 后即可算出向上取整的页数。
        // 例如 21 条数据每页放 10 条，需要 3 页。
        long pages = (total + pageSize - 1) / pageSize;
        pageResult.setPages(pages);

        return pageResult;
    }

}


