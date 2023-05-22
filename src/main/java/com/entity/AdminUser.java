package com.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@TableName(value = "admin_table")
@JsonIgnoreProperties({"handler", "hibernateLazyInitializer"})
public class AdminUser {

    @TableField(value = "zhanghao")
    @TableId(type = IdType.AUTO)
    private Integer zhanghao;        // 账号
    private String password;         // 密码
    private String name;             // 昵称
    private String email;            // 邮箱
    private String content;          // 描述
    private String sex;              // 性别
    private String wenti;            // 密保问题
    private String mibao;            // 密保答案
    private String ctime;            // 创建时间


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

    public Integer getZhanghao() {
        return zhanghao;
    }

    public void setZhanghao(Integer zhanghao) {
        this.zhanghao = zhanghao;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
