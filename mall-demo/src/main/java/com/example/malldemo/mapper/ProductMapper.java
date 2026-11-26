package com.example.malldemo.mapper;
import com.example.malldemo.entity.Product;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ProductMapper {

    // 对应 ProductMapper.xml 中的 findById。@Param 的名字和 SQL 里的 #{id} 对应。
    Product findById(@Param("id") Long id);



    /**
     * 在一条 SQL 中检查库存并扣减，避免把判断和扣减分成两个操作。
     * @param productId 商品id
     * @param quantity 要扣减的数量
     * @return 受影响行数，0 表示没有满足更新条件的商品记录
     */
    int deductStock(@Param("productId") Long productId,@Param("quantity")Integer quantity);

}
