package com.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.entity.MajorModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Map;


public interface MajorMapper extends BaseMapper<MajorModel> {


    // 根据 majorid 删除
    Integer deleteByMajorid(@Param("majorid") Integer majorid);


    // 根据 collegename 获取 专业信息
    List<Map<String,Object>> getMajorByCollegename(@Param("collegename") String collegename);


    // 根据 collegeid 获取 major 信息
    List<MajorModel> findByCollegeid(@Param("collegeid") Integer collegeid);


    // 根据 majorid 获取 majorname
    MajorModel findByMajorid(@Param("majorid") Integer majorid);


    // 根据 majorname 获取 majorid
    MajorModel findByMajorname(@Param("majorname")String majorname);


    // 根据 majorid 获取 当前的专业信息，涉及到其它表的数据
    Map<String,Object> findByMJinMid(@Param("majorid") Integer majorid);


    // 查询 major_table 中 college 的数量，依次来进行条件筛选
    List<Object> getCollegeInMajor();


    /**
     * 获取所有专业信息，条件查询  用于管理员页面，动态查询数据
     * 查询条件： like   college
     * **/
    // 1. 没有查询条件
    List<Map<String,Object>> findMJNO();

    // 2. like
    List<Map<String,Object>> findMJLike(@Param("like") String like);

    // 3. college
    List<Map<String,Object>> findMJCollege(@Param("college") String college);

    // 4. like 和 college
    List<Map<String,Object>> findMJLikeAndCollege(@Param("like") String like, @Param("college") String college);

}
