package com.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;



@TableName("college_table")
@JsonIgnoreProperties({"handler", "hibernateLazyInitializer"})
public class CollegeModel {

    @TableId(type = IdType.AUTO)
    private Integer collegeid;       // 学院ID
    private String collegename;      // 学院名称
    private String content;
    private String ctime;

    public String getCtime() {
        return ctime;
    }

    public void setCtime(String ctime) {
        this.ctime = ctime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getCollegeid() {
        return collegeid;
    }

    public String getCollegename() {
        return collegename;
    }

    public void setCollegeid(Integer collegeid) {
        this.collegeid = collegeid;
    }

    public void setCollegename(String collegename) {
        this.collegename = collegename;
    }
}
