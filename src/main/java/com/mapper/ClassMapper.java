package com.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.entity.Classroom;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface ClassMapper extends BaseMapper<Classroom> {

    // 根据 教学楼信息 tung 获取上课教室信息
    List<String> getByClassroom();

    // 获取所有教室信息， like
    List<Classroom> findAllRooms(@Param("like") String like);

    // like
    List<Classroom> findByClassroomLike(@Param("like") String like);


    // 更具 classroom （教室名称） 检查，看是否有重复数据，没有再进行添加操作
    List<Classroom> classroomcheck(@Param("classname") String classname);

    // 根据 classroom （教室名称）进行删除数据
    Integer deleteClassroomByName(@Param("classname") String classname);

    List<Classroom> findAllRoomsByName(@Param("query") String query);

    List<Classroom> findAllRooms2();
}
