package com.example.malldemo.mapper;
import com.example.malldemo.entity.Product;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ProductMapper {

    Product findById(@Param("id") Long id);



    /**
     * 原子扣减库存
     * @param productId 商品id
     * @param quantity 要扣减的数量
     * @return 数据库受影响行数，0=扣减失败（库存不足）
     */
    int deductStock(@Param("productId") Long productId,@Param("quantity")Integer quantity);

}
