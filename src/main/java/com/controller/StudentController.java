package com.controller;


import com.entity.CurriModel;
import com.entity.SelectionModel;
import com.entity.StudentUser;
import com.mapper.CurriMapper;
import com.mapper.SelectionMapper;
import com.mapper.StudentUserMapper;
import com.service.UserService;
import com.utils.MD5;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;

import javax.mail.internet.MimeMessage;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@RestController
public class StudentController {

    /**
     * 获取当前账户的已选课程信息 (有对课程名称进行模糊查和对课程类型进行筛选的条件)
     **/
    @Autowired
    StudentUserMapper studentUserDAO;

    @CrossOrigin
    @ResponseBody
    @PostMapping(value = "/api/sutdent/getselectionself", produces = "application/json;charset=UTF-8")
    public List<Map<String, Object>> SelectionSelfQuery(@RequestBody Map<String, Object> json) {
        String kechengleixing = String.valueOf(json.get("kechengleibie"));
        String like = String.valueOf(json.get("like"));
        Integer xuehao = Integer.valueOf((String) json.get("xuehao"));
        String xuefen = String.valueOf(json.get("xuefen"));

        List<Map<String, Object>> list = null;
        // 情况一： 没有任何筛选条件
        if (StringUtils.isEmpty(kechengleixing) && StringUtils.isEmpty(like) && StringUtils.isEmpty(xuefen)) {
            list = studentUserDAO.SelecttionNo(xuehao);
        }
        // 情况二： 模糊查询
        if (!StringUtils.isEmpty(like) && StringUtils.isEmpty(kechengleixing) && StringUtils.isEmpty(xuefen)) {
            list = studentUserDAO.SelectionLike(xuehao, like);
        }
        // 情况三： 学分
        if (!StringUtils.isEmpty(xuefen) && StringUtils.isEmpty(kechengleixing) && StringUtils.isEmpty(like)) {
            list = studentUserDAO.SelectionXuefen(xuehao, Integer.valueOf(xuefen));
        }
        // 情况四： 课程类别
        if (!StringUtils.isEmpty(kechengleixing) && StringUtils.isEmpty(like) && StringUtils.isEmpty(xuefen)) {
            list = studentUserDAO.SelectionLeixing(xuehao, kechengleixing);
        }
        // 情况五： 学分和模糊查询
        if (!StringUtils.isEmpty(xuefen) && !StringUtils.isEmpty(like) && StringUtils.isEmpty(kechengleixing)) {
            list = studentUserDAO.SelectionXandLike(xuehao, Integer.valueOf(xuefen), like);
        }
        // 情况六：学分和课程类型
        if (!StringUtils.isEmpty(xuefen) && !StringUtils.isEmpty(kechengleixing) && StringUtils.isEmpty(like)) {
            list = studentUserDAO.SelectionXandLeibie(xuehao, Integer.valueOf(xuefen), kechengleixing);
        }
        // 情况七： like 和 课程类型
        if (!StringUtils.isEmpty(like) && !StringUtils.isEmpty(kechengleixing) && StringUtils.isEmpty(xuefen)) {
            list = studentUserDAO.SelectionLandLiebie(xuehao, like, kechengleixing);
        }
        // 情况八： like 学分 课程类型
        if (!StringUtils.isEmpty(like) && !StringUtils.isEmpty(xuefen) && !StringUtils.isEmpty(kechengleixing)) {
            list = studentUserDAO.SelectionAll(xuehao, like, Integer.valueOf(xuefen), kechengleixing);
        }

        //a.ctime,a.id,b.kechengid,b.kechengname,b.kechengleibie,c.`name`,b.xuefen
        //String[] columnArray = new String[]{"ctime", "id", "kechengid", "kechengname", "kechengleibie", "name", "xuefen"};
        return list;
    }


    /**
     * 根据课程ID进行查询
     **/
    @Autowired
    CurriMapper curriDAO;

    @CrossOrigin
    @GetMapping("/api/student/getcurri/{kechengid}")
    public CurriModel getCurriInfo(@PathVariable("kechengid") Integer kechengid) {
        Optional<CurriModel> optional = Optional.ofNullable(curriDAO.findByKechengid(kechengid));
        return optional.orElseGet(CurriModel::new);
    }


    /**
     * 根据选课表的ID进行删除指定ID的记录
     **/
    @Autowired
    SelectionMapper selectionDAO;

    @CrossOrigin
    @DeleteMapping("/api/student/deleteselection/{id}")
    public Integer deleteSelection(@PathVariable("id") Integer id) {
        return selectionDAO.deleteById1(id);
    }


    /**
     * 根据学号获取该生的选课信息，用于生成课程表
     **/
    @CrossOrigin
    @GetMapping("/api/student/getkechengbiao/{xuehao}")
    public List<Map<String, Object>> getKeChengBiaoInfo(@PathVariable("xuehao") Integer xuehao) {
        //c.kechengname,c.kechengleibie,c.classroom,c.xuefen,t.name,c.startweek,c.endweek,c.classweek,c.classsection
        //String[] columnArray = new String[]{"kechengname", "kechengleibie", "classroom", "xuefen", "name", "startweek", "endweek", "classweek", "classsection"};
        return selectionDAO.getkechengbiao(xuehao);
    }


    /***
     * 获取当前账户的学生个人信息
     * **/
    @CrossOrigin
    @GetMapping("/api/student/getself/{xuehao}")
    public StudentUser getStuSelf(@PathVariable("xuehao") Integer xuehao) {
        Optional<StudentUser> optional = Optional.ofNullable(studentUserDAO.findByXuehao(xuehao));
        return optional.orElseGet(StudentUser::new);
    }


    /**
     * 根据账户信息进行基本信息更新
     **/
    @CrossOrigin
    @PutMapping("/api/student/updateself/{xuehao}")
    public StudentUser updateStuSelf(@PathVariable("xuehao") Integer xuehao, @RequestBody StudentUser studentUser) {
        studentUser.setXuehao(Integer.valueOf(xuehao));
        studentUserDAO.updateById(studentUser);
        return studentUser;
    }


    /**
     * 根据学号对密码进行重置，用于忘记密码之后找回的时候充值密码
     **/
    @CrossOrigin
    @PostMapping("/api/student/resetpwd")
    public Object ResetPassword(@RequestBody Map<String, Object> json) throws Exception {
        String zhanghu = String.valueOf(json.get("zhanghu"));
        String password = String.valueOf(json.get("password"));
        StudentUser studentUser = studentUserDAO.findByXuehao(Integer.valueOf(zhanghu));
        if (studentUser == null) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("msg", "数据有误 !请检查后重试！");
            jsonObject.put("code", "255");
            return jsonObject.toString();
        } else {
            studentUser.setPassword(MD5.md5(password));
            studentUserDAO.updateById(studentUser);
            if (studentUser == null) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("msg", "重置密码失败!");
                jsonObject.put("code", "255");
                return jsonObject.toString();
            } else {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("msg", "重置密码成功!");
                jsonObject.put("code", "200");
                return jsonObject.toString();
            }
        }
    }


    /**
     * 根据账户信息对密码进行更新
     **/
    @CrossOrigin
    @PutMapping("/api/student/updateselfpwd/{xuehao}")
    public StudentUser updateStuPwd(@PathVariable("xuehao") Integer xuehao, @RequestBody StudentUser studentUser) throws Exception {
        studentUser.setXuehao(xuehao);
        studentUser.setPassword(MD5.md5(studentUser.getPassword()));
        studentUserDAO.updateById(studentUser);
        return studentUser;
    }


    /**
     * 获取当前学生账户所在班级的可选课程
     **/
    @CrossOrigin
    @ResponseBody
    @PostMapping("/api/sutdent/getselectionall")
    public List<Map<String, Object>> SelectionALLQuery(@RequestBody Map<String, Object> json) {
        String kechengleixing = String.valueOf(json.get("kechengleibie"));
        String like = String.valueOf(json.get("like"));
        Integer xuehao = Integer.valueOf((String) json.get("xuehao"));
        String xuefen = String.valueOf(json.get("xuefen"));

        List<Map<String, Object>> list = null;
        // 情况一： 没有任何筛选条件
        if (StringUtils.isEmpty(kechengleixing) && StringUtils.isEmpty(like) && StringUtils.isEmpty(xuefen)) {
            list = curriDAO.findCurriNo(xuehao);
        }
        // 情况二： 模糊查询
        if (!StringUtils.isEmpty(like) && StringUtils.isEmpty(kechengleixing) && StringUtils.isEmpty(xuefen)) {
            list = curriDAO.findCurriLike(xuehao, like);
        }
        // 情况三： 学分
        if (!StringUtils.isEmpty(xuefen) && StringUtils.isEmpty(kechengleixing) && StringUtils.isEmpty(like)) {
            list = curriDAO.findCurriXuefen(xuehao, Integer.valueOf(xuefen));
        }
        // 情况四： 课程类别
        if (!StringUtils.isEmpty(kechengleixing) && StringUtils.isEmpty(like) && StringUtils.isEmpty(xuefen)) {
            list = curriDAO.findCurriLeixing(xuehao, kechengleixing);
        }
        // 情况五： 学分和模糊查询
        if (!StringUtils.isEmpty(xuefen) && !StringUtils.isEmpty(like) && StringUtils.isEmpty(kechengleixing)) {
            list = curriDAO.findCurriXandLike(xuehao, like, Integer.valueOf(xuefen));
        }
        // 情况六：学分和课程类型
        if (!StringUtils.isEmpty(xuefen) && !StringUtils.isEmpty(kechengleixing) && StringUtils.isEmpty(like)) {
            list = curriDAO.findCurriXandLeixing(xuehao, Integer.valueOf(xuefen), kechengleixing);
        }
        // 情况七： like 和 课程类型
        if (!StringUtils.isEmpty(like) && !StringUtils.isEmpty(kechengleixing) && StringUtils.isEmpty(xuefen)) {
            list = curriDAO.findCurriLandLeibie(xuehao, like, kechengleixing);
        }
        // 情况八： like 学分 课程类型
        if (!StringUtils.isEmpty(like) && !StringUtils.isEmpty(xuefen) && !StringUtils.isEmpty(kechengleixing)) {
            list = curriDAO.findCurriAll(xuehao, like, kechengleixing, Integer.valueOf(xuefen));
        }

        //b.ctime,b.kechengid,b.classsection,b.kechengname,b.xuefen,(SELECT t.`name` from teacher_table as t where b.kechengteacher=t.zhanghu) as 'teachername',
        // b.kechengleibie,b.startweek,b.endweek,b.classroom,b.classweek,(SELECT COUNT(*) from selection_table as s where s.kechengid=b.kechengid and s.xuehao=:xuehao) as 'selection'
        //String[] columnArray = new String[]{"ctime", "kechengid", "classsection", "kechengname", "xuefen", "teachername", "kechengleibie", "startweek", "endweek", "classroom", "classweek", "selection"};
        return list;
    }


    /**
     * 学生自主选课，将选课信息添加到 选课信息表
     * **/
    /**
     * 添加学生用户信息
     **/
    @CrossOrigin
    @ResponseBody
    @PostMapping(value = "/api/student/addsutclass", produces = "application/json;charset=UTF-8")
    public SelectionModel addSelection(@RequestBody Map<String, Object> json) {
        String xuehao = String.valueOf(json.get("xuehao"));
        String kechengid = String.valueOf(json.get("kechengid"));
        SelectionModel selectionModel = new SelectionModel();
        selectionModel.setXuehao(Integer.valueOf(xuehao));
        selectionModel.setKechengid(Integer.valueOf(kechengid));
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        selectionModel.setCtime(df.format(new Date()));
        selectionDAO.insert(selectionModel);
        return selectionModel;
    }


    /**
     * 根据当前登录的学生账号，获取该学生当前选择的学分情况，分为选修和必修
     ***/
    // 查找必修
    @CrossOrigin
    @PostMapping("/api/student/getxuefenbi/{zhanghao}")
    private List<Map<String, Object>> getXueFenBi(@PathVariable("zhanghao") Integer zhanghao) {
        //c.kechengname,c.xuefen
        //String[] column = new String[]{"kechengname", "xuefen"};
        return selectionDAO.getXueFenBixiu(zhanghao);
    }

    // 查找必修
    @CrossOrigin
    @PostMapping("/api/student/getxuefenxuan/{zhanghao}")
    private List<Map<String, Object>> getXueFenXuan(@PathVariable("zhanghao") Integer zhanghao) {
        //c.kechengname,c.xuefen
        //String[] column = new String[]{"kechengname", "xuefen"};
        return selectionDAO.getXueFenXuanxiu(zhanghao);
    }


    /**
     * 学生找回密码操作
     **/
    // 注入QQ发送邮件的bean
    @Autowired
    JavaMailSender mailSender;
    @Autowired
    UserService userService;

    @CrossOrigin
    @ResponseBody
    @PostMapping(value = "/api/student/forgetepwd", produces = "application/json;charset=UTF-8")
    public Object sendEmail(@RequestBody Map<String, Object> json) {
        String qq = String.valueOf(json.get("email"));
        Integer zhanghu = Integer.valueOf(String.valueOf(json.get("zhanghao")));
        String mibao = String.valueOf(json.get("mibao"));
//        StudentUser studentUser = studentUserDAO.findByXuehao(zhanghu);
        StudentUser studentUser = studentUserDAO.findByXuehaoAndEmailAndMibao(zhanghu, qq, mibao);
        // 账号不存在
        if (studentUser == null) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("msg", "账户和邮箱数据有误 !");
            jsonObject.put("code", "255");
            return jsonObject.toString();
        } else {
            Object jsonStr = userService.sendEmailHanle("student", zhanghu, studentUser.getName(), studentUser.getEmail(), qq, 3);
            if (jsonStr.equals("")) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("msg", "邮件发送失败！出现未知错误！");
                jsonObject.put("code", "256");
                return jsonObject.toString();
            }
            return jsonStr;
        }

    }


}
