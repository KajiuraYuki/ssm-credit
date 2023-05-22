package com.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.entity.CollegeModel;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface CollegeMapper extends BaseMapper<CollegeModel> {

    // 根据 collegeid 获取当前的 collegename
    CollegeModel findByCollegeid(@Param("collegeid") Integer collegeid);

    // 根据 collegename 获取当前的 collegeid
    CollegeModel findByCollegename(@Param("collegename") String collegename);

    /**
     * 获取每个学院的学生和教师信息
     * 用于管理员数据统计页面
     **/
    List<Map<String, Object>> getAllNums();

    // like
    List<CollegeModel> findByCollegenameLike(@Param("like") String like);

    // 根据 collegeid 删除
    Integer deleteByCollegeid(@Param("collegeid") Integer collegeid);

    List<CollegeModel> findAll();

}
