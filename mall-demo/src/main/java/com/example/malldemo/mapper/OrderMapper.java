package com.example.malldemo.mapper;
import com.example.malldemo.dto.OrderDetailVO;
import com.example.malldemo.dto.OrderQueryDTO;
import com.example.malldemo.dto.OrderStatusUpdateDTO;
import com.example.malldemo.dto.PageResult;
import com.example.malldemo.entity.Orders;
import jakarta.validation.Valid;
import org.apache.ibatis.annotations.*;

import java.util.List;


/** 新增订单使用下方的注解 SQL，其余语句在 OrderMapper.xml，XML 的 id 对应接口方法名。 */
@Mapper

public interface OrderMapper  {
    // #{} 从 Orders 对象取字段值，插入成功后会把数据库生成的主键写回 id。
    @Insert("INSERT INTO orders(user_id,product_id,product_name,quantity,unit_price,total_price,order_status,create_time)"+"VALUES(#{userId},#{productId}, #{productName},#{quantity},#{unitPrice}, #{totalPrice}, #{orderStatus}, #{createTime})")
    @Options(useGeneratedKeys = true,keyProperty = "id")
    int insertOrder(Orders orders);


    // 读取订单本身，供状态更新时校验归属和当前状态。
    Orders selectOrderById(Long orderId);

   List<Orders>selectOrderPageByUser(@Param("userId") Long userId,
                                     @Param("offset") Integer offset,
                                     @Param("pageSize") Integer pageSize);
   Long countOrderByUser(@Param("userId")Long userId);

   int updateOrderStatus(@Param("orderId")Long orderId,@Param("newStatus")Integer  newStatus);
   OrderDetailVO selectOrderDetailById(@Param("orderId") Long orderId);

}
