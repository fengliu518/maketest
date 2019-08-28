package com.example.mapper;

import com.example.entity.User;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;


@Repository
public interface UserMapper {

    User Sel(int id);

    User login(String userName,String passWord);

    int register(User user);

}


