package com.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.entity.BanjiModel;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;


public interface BanjiMapper extends BaseMapper<BanjiModel> {

    List<Map<String,Object>> getBanjiByMajorname(@Param("majorname") String majorname);

    // 根据 majorid 获取该专业所有班级
    List<BanjiModel> findByMajorid(@Param("majorid") Integer majorid);

    // 根据 banjiid 获取 banjiname
    BanjiModel findByBanjiid(@Param("banjiid")Integer banjiid);


    Map<String,Object> findByBanjiidA(@Param("banjiid") Integer banjiid);

    List<BanjiModel> findByZhanghu(@Param("zhanghu") Integer zhanghu);

    Integer deleteByBanjiid(@Param("banjiid") Integer banjiid);

    List<Map<String,Object>> findBJNO();

    List<Map<String,Object>> findBJLike(@Param("like") String like);

    List<Map<String,Object>> findBJCollege(@Param("collegename") String collegename);

    List<Map<String,Object>> findBJMajor(@Param("majorname") String majorname);

    List<Map<String,Object>> findBJLikeAndCollege(@Param("like") String like,@Param("collegename") String collegename);

    List<Map<String,Object>> findBJLikeAndMajor(@Param("like") String like,@Param("majorname") String majorname);

    List<Map<String,Object>> findBJCollegeAndMajor(@Param("collegename") String collegename,@Param("majorname") String majorname);

    List<Map<String,Object>> findBJLAll(@Param("like") String like,@Param("collegename") String collegename,@Param("majorname") String majorname);
}
