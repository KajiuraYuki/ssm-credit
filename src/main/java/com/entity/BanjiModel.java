package com.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


@TableName(value = "banji_table")
@JsonIgnoreProperties({"handler", "hibernateLazyInitializer"})
public class BanjiModel {
    @TableId(type = IdType.AUTO)
    private Integer banjiid;      // 班级ID
    private String banjiname;     // 班级名称
    private Integer majorid;      // 专业ID
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

    public Integer getBanjiid() {
        return banjiid;
    }

    public void setBanjiid(Integer banjiid) {
        this.banjiid = banjiid;
    }

    public String getBanjiname() {
        return banjiname;
    }

    public void setBanjiname(String banjiname) {
        this.banjiname = banjiname;
    }

    public Integer getMajorid() {
        return majorid;
    }

    public void setMajorid(Integer majorid) {
        this.majorid = majorid;
    }
}
