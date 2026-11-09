package com.example.malldemo.mapper;
import com.example.malldemo.entity.Orders;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;


@Mapper

public interface OrderMapper  {
    @Insert("INSERT INTO orders(user_id,product_id,quantity,total_price,order_status,create_time)"+"VALUES(#{userId},#{productId}, #{quantity}, #{totalPrice}, #{orderStatus}, #{createTime})")
    @Options(useGeneratedKeys = true,keyProperty = "id")
    int insertOrder(Orders orders);

    @Select("select id,user_id,product_id,quantity,total_price,order_status,create_time from orders where id = #{orderId}")
    Orders selectOrderById(Long orderId);
}
