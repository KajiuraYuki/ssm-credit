package com.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.entity.SelectionModel;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;
import java.util.Optional;


public interface SelectionMapper extends BaseMapper<SelectionModel> {


    /**
     * 根据当前学生账号查看当前学生已选则的学分情况，分为选修和必修
     **/
    // 查找必修
    List<Map<String, Object>> getXueFenBixiu(@Param("xuehao") Integer xuehao);

    // 查找选修
    List<Map<String, Object>> getXueFenXuanxiu(@Param("xuehao") Integer xuehao);


    // 根据学号获取该生的选课信息，用于生成课程表
    List<Map<String, Object>> getkechengbiao(@Param("xuehao") Integer xuehao);

    // 根据id查询选课记录
    Optional<SelectionModel> findById(Integer id);

    // 删除选课表中的重复数据
    Integer deleteRepeat();

    // 根据id删除选课信息
    Integer deleteById1(@Param("id") Integer id);

    // 删除原来有选课班级，编辑时取消了班级的学生已选课程
    Integer deleteRepeat1(@Param("str") String str, @Param("kechengid") Integer kechengid);


    // 没有对课程名称的模糊查询条件
    List<Map<String, Object>> SelectionClass(@Param("kechengteacher") String kechengteacher);

    // 没有对课程名称的模糊查询条件，  对 课程类型为  必修 进行筛选
    List<Map<String, Object>> SelectionClassBixiu(@Param("kechengteacher") String kechengteacher);

    // 没有对课程名称的模糊查询条件，  对 课程类型为  选修 进行筛选
    List<Map<String, Object>> SelectionClassXuanxiu(@Param("kechengteacher") String kechengteacher);


    // 有对课程名称的迷糊查询条件
    List<Map<String, Object>> SelectionClassLike(@Param("kechengteacher") String kechengteacher, @Param("kechengname") String kechengname);

    // 有对课程名称的迷糊查询条件  对 课程类型为  必修 进行筛选
    List<Map<String, Object>> SelectionClassLikeBiXiu(@Param("kechengteacher") String kechengteacher, @Param("kechengname") String kechengname);

    // 有对课程名称的迷糊查询条件  对 课程类型为  选修 进行筛选
    List<Map<String, Object>> SelectionClassLikeXuanXiu(@Param("kechengteacher") String kechengteacher, @Param("kechengname") String kechengname);


}
