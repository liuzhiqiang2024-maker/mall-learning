package com.example.malldemo.mapper;
import com.example.malldemo.dto.OrderQueryDTO;
import com.example.malldemo.dto.OrderStatusUpdateDTO;
import com.example.malldemo.dto.PageResult;
import com.example.malldemo.entity.Orders;
import jakarta.validation.Valid;
import org.apache.ibatis.annotations.*;

import java.util.List;


@Mapper

public interface OrderMapper  {
    @Insert("INSERT INTO orders(user_id,product_id,quantity,total_price,order_status,create_time)"+"VALUES(#{userId},#{productId}, #{quantity}, #{totalPrice}, #{orderStatus}, #{createTime})")
    @Options(useGeneratedKeys = true,keyProperty = "id")
    int insertOrder(Orders orders);


    Orders selectOrderById(Long orderId);

    List<Orders> selectOrderPage(@Param("offset")Integer offset,@Param("pageSize")Integer pageSize);
    Long selectOrderCount();

    List<Orders> selectOrderPage(@Param("query")OrderQueryDTO queryDTO,@Param("offset")Integer offset,@Param("pageSize")Integer pageSize);
    Long countOrderPage(@Param("query")OrderQueryDTO queryDTO);

    int updateOrderStatus(@Param("orderId")Long orderId,@Param("newStatus")Integer  newStatus);


}
