package com.example.malldemo.mapper;
import com.example.malldemo.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
@Mapper
public interface UserMapper {
    // @Param 给参数命名，UserMapper.xml 中的 #{username} 用它绑定登录名。
    User findByUsername(@Param("username") String username);
}
