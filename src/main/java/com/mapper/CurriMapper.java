package com.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.entity.CurriModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;


public interface CurriMapper extends BaseMapper<CurriModel> {

    CurriModel findByKechengid(@Param("kechengid") Integer kechengid);


    // 检测同一上课时间选择的上课教室是否冲突
    List<CurriModel> classroomcheck_curri(@Param("classroomname") String classroomname, @Param("classweek") String classweek, @Param("classsection") String classsection, @Param("startweek") String startweek, @Param("endweek") String endweek);


    // 根据当前登录的教师账号，获取该教师开设的所有课程中的选课人数
    List<Map<String, Object>> getSelectionNum(@Param("kechengteacher") Integer kechengteacher);


    // 根据课程id进行删除
    Integer deleteByKechengid(@Param("kechengid") Integer kechengid);


    /**
     * 获取当前账户的可选课程信息
     **/
    // 1. 没有查询条件
    List<Map<String, Object>> findCurriNo(@Param("xuehao") Integer xuehao);

    // 2. 只有筛选条件为 课程类型
    List<Map<String, Object>> findCurriLeixing(@Param("xuehao") Integer xuehao, @Param("kechengleibie") String kechengleibie);

    // 3. 只有筛选条件为 模糊查询
    List<Map<String, Object>> findCurriLike(@Param("xuehao") Integer xuehao, @Param("like") String like);

    // 4. 只有筛选条件为学分
    List<Map<String, Object>> findCurriXuefen(@Param("xuehao") Integer xuehao, @Param("xuefen") Integer xuefen);

    // 5. 筛选条件为 学分 和 模糊查询
    List<Map<String, Object>> findCurriXandLike(@Param("xuehao") Integer xuehao, @Param("like") String like, @Param("xuefen") Integer xuefen);

    // 6. 筛选条件为 学分 和 课程类型
    List<Map<String, Object>> findCurriXandLeixing(@Param("xuehao") Integer xuehao, @Param("xuefen") Integer xuefen, @Param("kechengleibie") String kechengleibie);

    // 7. 筛选条件为 模糊查询 和 课程类型
    List<Map<String, Object>> findCurriLandLeibie(@Param("xuehao") Integer xuehao, @Param("like") String like, @Param("kechengleibie") String kechengleibie);

    // 8. 筛选条件为 模糊查询 和 学分 以及 课程类型
    List<Map<String, Object>> findCurriAll(@Param("xuehao") Integer xuehao, @Param("like") String like, @Param("kechengleibie") String kechengleibie, @Param("xuefen") Integer xuefen);

    Integer classRoomcheckByClassRoomName(@Param("classRoomName") String classRoomName);

    List<CurriModel> findAll(@Param("kechengteacher")Integer kechengteacher, @Param("kechengname")String kechengname,
                             @Param("kechengleibie")String kechengleibie,@Param("pageNumber")int pageNumber,
                             @Param("pageSize")int pageSize);
}
