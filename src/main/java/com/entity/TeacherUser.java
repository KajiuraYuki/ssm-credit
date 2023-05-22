package com.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


@TableName(value = "teacher_table")
@JsonIgnoreProperties({"handler", "hibernateLazyInitializer"})
public class TeacherUser {
    @TableId(type = IdType.AUTO)
    private Integer zhanghu;   // 账户
    private String password;  // 密码
    private String sex;      // 性别
    private String email;     // 邮箱
    private String name;      // 姓名
    private String college;   // 所属院系
    private String major;     // 所属专业
    private String content;   // 个人描述
    private String wenti;     // 密保问题
    private String mibao;     // 密保答案
    private String ctime;


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

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getCollege() {
        return college;
    }

    public String getMajor() {
        return major;
    }

    public String getName() {
        return name;
    }

    public String getContent() {
        return content;
    }

    public void setCollege(String college) {
        this.college = college;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public Integer getZhanghu() {
        return zhanghu;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setZhanghu(Integer zhanghu) {
        this.zhanghu = zhanghu;
    }
}
