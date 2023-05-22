package com.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;


@TableName(value = "selection_table")
@JsonIgnoreProperties({"handler", "hibernateLazyInitializer"})
public class SelectionModel {

    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer xuehao;            // 选课表中的学号字段
    private Integer kechengid;         // 选课表中的课程ID字段
    private String ctime;


    public String getCtime() {
        return ctime;
    }

    public void setCtime(String ctime) {
        this.ctime = ctime;
    }

    public Integer getKechengid() {
        return kechengid;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getXuehao() {
        return xuehao;
    }

    public Integer getId() {
        return id;
    }

    public void setKechengid(Integer kechengid) {
        this.kechengid = kechengid;
    }

    public void setXuehao(Integer xuehao) {
        this.xuehao = xuehao;
    }
}
