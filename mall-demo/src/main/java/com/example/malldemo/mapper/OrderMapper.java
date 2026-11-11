package com.example.malldemo.mapper;
import com.example.malldemo.dto.PageResult;
import com.example.malldemo.entity.Orders;
import org.apache.ibatis.annotations.*;

import java.util.List;


@Mapper

public interface OrderMapper  {
    @Insert("INSERT INTO orders(user_id,product_id,quantity,total_price,order_status,create_time)"+"VALUES(#{userId},#{productId}, #{quantity}, #{totalPrice}, #{orderStatus}, #{createTime})")
    @Options(useGeneratedKeys = true,keyProperty = "id")
    int insertOrder(Orders orders);

    @Select("select id,user_id,product_id,quantity,total_price,order_status,create_time from orders where id = #{orderId}")
    Orders selectOrderById(Long orderId);

    List<Orders> selectOrderPage(@Param("offset")Integer offset,@Param("pageSize")Integer pageSize);
    Long selectOrderCount();

}
