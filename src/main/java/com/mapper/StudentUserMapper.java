package com.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.entity.StudentUser;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;


public interface StudentUserMapper extends BaseMapper<StudentUser> {

    List<StudentUser> findAll(Map<String,Object> map);

    // 根据学号和邮箱获取学生信息
    StudentUser findByXuehaoAndEmail(@Param("xuehao") Integer xuehao, @Param("email")String email);

    // 根据学号、邮箱、密保进行查找密码
    StudentUser findByXuehaoAndEmailAndMibao(@Param("xuehao")Integer xuehao, @Param("email")String email, @Param("mibao")String mibao);

    StudentUser findByXuehao(@Param("xuehao")Integer xuehao);

    // 根据学号删除学生信息
    // 根据账户信息删除指定的教师账号
    Integer deleteByXuehao(@Param("xuehao") Integer xuehao);

    StudentUser getByXuehaoAndPassword(Integer xuehao, String password);

    // 学生管理页面   获取学院名称
    List<String> getByCollegegroup();

    // 学生管理页面    获取专业名称
    List<String> getByMajorgroup();


    // 学生管理页面    获取班级名称
    List<String> getByBanjigroup();

    // 根据班级字段获取该班级所有学号信息
    List<Integer> findByXhinBJ(@Param("banji") String banji);


    // 查询当前课程所有选课学生信息（参数只有课程id）
    List<Map<String, Object>> getAllSelection(@Param("kechengid") Integer kechengid);


    /**
     * 获取当前学生账号的所有选课信息
     **/
    // 1. 没有筛选条件
    List<Map<String, Object>> SelecttionNo(@Param("xuehao") Integer xuehao);

    // 2. 只有筛选条件为 课程类型
    List<Map<String, Object>> SelectionLeixing(@Param("xuehao") Integer xuehao, @Param("kechengleibie") String kechengleibie);

    // 3. 只有筛选条件为 模糊查询
    List<Map<String, Object>> SelectionLike(@Param("xuehao") Integer xuehao, @Param("like") String like);

    // 4. 只有筛选条件为 学分
    List<Map<String, Object>> SelectionXuefen(@Param("xuehao") Integer xuehao, @Param("xuefen") Integer xuefen);

    // 5. 筛选条件为 学分 和 模糊查询
    List<Map<String, Object>> SelectionXandLike(@Param("xuehao") Integer xuehao, @Param("xuefen") Integer xuefen, @Param("like") String like);

    // 6. 筛选条件为 学分和课程类型
    List<Map<String, Object>> SelectionXandLeibie(@Param("xuehao") Integer xuehao, @Param("xuefen") Integer xuefen, @Param("kechengleibie") String kechengleibie);

    // 7. 筛选条件为 模糊查询 和 课程类型
    List<Map<String, Object>> SelectionLandLiebie(@Param("xuehao") Integer xuehao, @Param("like") String like, @Param("kechengleibie") String kehcengleibie);

    // 8. 筛选条件为 模糊查询 和 学分 以及 课程类型
    List<Map<String, Object>> SelectionAll(@Param("xuehao") Integer xuehao, @Param("like") String like, @Param("xuefen") Integer xuefen, @Param("kechengleibie") String kechengleibie);
}
