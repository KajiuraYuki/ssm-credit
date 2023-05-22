package com.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;



@TableName(value = "curriculum_table")
@JsonIgnoreProperties({"handler", "hibernateLazyInitializer"})
public class CurriModel {

    @TableId(type = IdType.AUTO)
    private Integer kechengid;             // 课程id
    private String kechengname;        // 课程名称
    private String kechengleibie;      // 课程类别
    private Integer xuefen;                // 课程学分
    private Integer kechengteacher;     // 开课教师
    private String classroom;          // 上课教室
    private String startweek;          // 开始周次
    private String endweek;          // 结束周次
    private String classweek;          // 上课周次
    private String classsection;          // 上课节次
    private String optionalclass;       // 可选班级
    private String ctime;

    public String getCtime() {
        return ctime;
    }

    public void setCtime(String ctime) {
        this.ctime = ctime;
    }

    public String getOptionalclass() {
        return optionalclass;
    }

    public void setOptionalclass(String optionalclass) {
        this.optionalclass = optionalclass;
    }

    public void setClasssection(String classsection) {
        this.classsection = classsection;
    }

    public void setClassweek(String classweek) {
        this.classweek = classweek;
    }

    public void setEndweek(String endweek) {
        this.endweek = endweek;
    }

    public void setStartweek(String startweek) {
        this.startweek = startweek;
    }

    public String getClasssection() {
        return classsection;
    }

    public String getClassweek() {
        return classweek;
    }

    public String getEndweek() {
        return endweek;
    }

    public String getStartweek() {
        return startweek;
    }

    public Integer getKechengteacher() {
        return kechengteacher;
    }

    public String getClassroom() {
        return classroom;
    }

    public String getKechengleibie() {
        return kechengleibie;
    }

    public String getKechengname() {
        return kechengname;
    }

    public Integer getKechengid() {
        return kechengid;
    }

    public int getXuefen() {
        return xuefen;
    }


    public void setXuefen(Integer xuefen) {
        this.xuefen = xuefen;
    }

    public void setKechengid(Integer kechengid) {
        this.kechengid = kechengid;
    }

    public void setKechengteacher(Integer kechengteacher) {
        this.kechengteacher = kechengteacher;
    }

    public void setClassroom(String classroom) {
        this.classroom = classroom;
    }

    public void setXuefen(int xuefen) {
        this.xuefen = xuefen;
    }

    public void setKechengleibie(String kechengleibie) {
        this.kechengleibie = kechengleibie;
    }

    public void setKechengname(String kechengname) {
        this.kechengname = kechengname;
    }

}
