package com.example.malldemo.mapper;
import com.example.malldemo.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
@Mapper
public interface UserMapper {
    User findByUsername(@Param("username") String username);
}
