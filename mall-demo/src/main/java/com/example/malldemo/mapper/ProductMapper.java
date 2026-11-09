package com.example.malldemo.mapper;
import com.example.malldemo.entity.Product;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ProductMapper {
    @Select("SELECT id,name,price,stock,status,create_time FROM product WHERE id= #{id}")
    Product findById(@Param("id") Long id);

    @Update("UPDATE product SET stock =stock - #{count} WHERE id =#{id} AND stock >=#{count}")
    int deductStock(@Param("id") Long id, @Param("count") Integer count);
}
