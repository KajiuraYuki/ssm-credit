package com.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


@TableName(value = "reset_pwd_table")
@JsonIgnoreProperties({"handler", "hibernateLazyInitializer"})
public class ResetPwdModel {
    @TableId(type = IdType.AUTO)
    private Integer zhanghu; // 账号
    private Integer type;       // 账号类型
    private String code;    // 验证码
    private String s_time;  // 有效时间
    private Integer state_code; // 状态码

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getZhanghu() {
        return zhanghu;
    }

    public void setZhanghu(Integer zhanghu) {
        this.zhanghu = zhanghu;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getS_time() {
        return s_time;
    }

    public void setS_time(String s_time) {
        this.s_time = s_time;
    }

    public Integer getState_code() {
        return state_code;
    }

    public void setState_code(Integer state_code) {
        this.state_code = state_code;
    }
}
