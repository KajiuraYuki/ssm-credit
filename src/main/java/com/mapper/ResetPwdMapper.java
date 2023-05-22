package com.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.entity.ResetPwdModel;
import org.apache.ibatis.annotations.Param;



public interface ResetPwdMapper extends BaseMapper<ResetPwdModel> {
    /**
     * 根据用户名获取重置密码表的模型
     **/
    ResetPwdModel findByZhanghu(@Param("zhanghu") Integer zhanghu);


    /**
     * 根据 用户名、账户类型、验证码 查找用户
     **/
    ResetPwdModel findByZhanghuAndTypeAndCode(@Param("zhanghu")Integer zhanghu, @Param("type")Integer type, @Param("code")String code);


    /**
     * 根据账户和类型进行查找
     **/
    ResetPwdModel findByZhanghuAndType(@Param("zhanghu")Integer zhanghu,@Param("type") Integer type);


}
