package com.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.entity.TeacherUser;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface TeacherUserMapper extends BaseMapper<TeacherUser> {
    TeacherUser findByZhanghu(@Param("zhanghu") Integer zhanghu);

    // 根据账号、邮箱、密保进行查找账户
    TeacherUser findByZhanghuAndEmailAndMibao(@Param("zhanghu") Integer zhanghu, @Param("email") String email, @Param("mibao") String mibao);


    TeacherUser getByZhanghuAndPassword(@Param("zhanghu") Integer zhanghu, @Param("password") String password);

    TeacherUser findByZhanghuAndEmail(@Param("zhanghu") Integer zhanghu, @Param("email") String email);


    // 教师管理页面   获取学院名称
    List<String> getByCollegegroupJjs();

    // 教师管理页面    获取专业名称
    List<String> getByMajorgroupjs();


    // 根据账户信息删除指定的教师账号
    Integer deleteByZhanghu(@Param("zhanghu") Integer zhanghu);

    List<TeacherUser> findAll(Map<String,Object> map);
}
