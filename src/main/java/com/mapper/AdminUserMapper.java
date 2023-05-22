package com.mapper;


import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.entity.AdminUser;
import org.apache.ibatis.annotations.Param;


public interface AdminUserMapper extends BaseMapper<AdminUser> {

    AdminUser findByZhanghao(@Param("zhanghao") Integer zhanghao);

    // 根据账户、邮箱、密保进行查找用户
    AdminUser findByZhanghaoAndEmailAndMibao(@Param("zhanghao") Integer zhanghao, @Param("email") String email, @Param("mibao") String mibao);

    AdminUser getByZhanghaoAndPassword(@Param("zhanghao") Integer zhanghao, @Param("password") String password);

    AdminUser findByZhanghaoAndEmail(@Param("zhanghao") Integer zhanghao, @Param("email") String email);

}
