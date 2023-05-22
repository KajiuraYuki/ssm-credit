package com.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;


@TableName(value = "student_table")
@JsonIgnoreProperties({"handler", "hibernateLazyInitializer"})
public class StudentUser {
    @TableId(type = IdType.AUTO)
    private Integer xuehao;    // 学号
    private String password;   // 密码
    private String name;       // 姓名
    private Integer age;       // 年龄
    private String sex;        // 性别
    private String college;    // 学院
    private String major;      // 专业
    private String banji;      // 班级
    private String email;      // 邮箱
    private String content;    // 描述
    private String wenti;      // 密保问题
    private String mibao;      // 密保答案
    private String ctime;      // 创建时间

    public String getWenti() {
        return wenti;
    }

    public void setWenti(String wenti) {
        this.wenti = wenti;
    }

    public String getMibao() {
        return mibao;
    }

    public void setMibao(String mibao) {
        this.mibao = mibao;
    }

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getXuehao() {
        return xuehao;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Integer getAge() {
        return age;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getMajor() {
        return major;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getBanji() {
        return banji;
    }

    public void setXuehao(Integer xuehao) {
        this.xuehao = xuehao;
    }

    public String getCollege() {
        return college;
    }

    public void setBanji(String banji) {
        this.banji = banji;
    }

    public String getSex() {
        return sex;
    }

    public void setCollege(String college) {
        this.college = college;
    }
}
