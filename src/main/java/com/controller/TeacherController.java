package com.controller;

import com.baomidou.mybatisplus.plugins.Page;
import com.entity.*;
import com.mapper.*;
import com.service.CurriService;
import com.service.UserService;
import com.utils.MD5;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.*;

import org.apache.commons.lang3.StringUtils;

import javax.mail.internet.MimeMessage;


@RestController
public class TeacherController {


    /**
     * 对所有学生信息进行动态分页查询
     **/
    @Autowired
    StudentUserMapper studentUserDAO;
    @Autowired
    UserService userService;

    @CrossOrigin
    @ResponseBody
    @PostMapping("/api/teacher/studentlist")
    public Page<StudentUser> pageQuery1(@RequestBody Map<String, Object> json) {
        Integer pageNum = Integer.valueOf((Integer) json.get("pageNum"));
        Integer pageSize = Integer.valueOf((Integer) json.get("pageSize"));
        String college = String.valueOf((String) json.get("college"));
        String major = String.valueOf(json.get("major"));
        String banji = String.valueOf(json.get("banji"));
        String like = String.valueOf(json.get("query"));
        String sex = String.valueOf(json.get("sex"));
        return userService.getStuList(like, college, major, banji, sex, pageNum,pageSize);
    }


    /***
     * 根据学号进行查询
     * **/
    @CrossOrigin
    @GetMapping("/api/teacher/getstu/{xuehao}")
    public StudentUser getUserInfo(@PathVariable("xuehao") Integer xuehao) {
        Optional<StudentUser> optional = Optional.ofNullable(studentUserDAO.findByXuehao(xuehao));
        return optional.orElseGet(StudentUser::new);
    }

    /**
     * 根据学号进行更新
     ***/
    @CrossOrigin
    @PutMapping("/api/teacher/updatestu/{xuehao}")
    public StudentUser updateStuUser(@PathVariable("xuehao") Integer xuehao, @RequestBody StudentUser studentUser) throws Exception {
        studentUser.setXuehao(xuehao);
        if (studentUser.getPassword().length() != 64) {
            studentUser.setPassword(MD5.md5(studentUser.getPassword()));
        }
        studentUserDAO.updateById(studentUser);
        return studentUser;
    }


    /**
     * 获取学院信息
     **/
    @CrossOrigin
    @GetMapping("/api/teacher/getcollegegroup")
    public List<String> findByCollegeGroup() {
        return studentUserDAO.getByCollegegroup();
    }


    /**
     * 获取专业信息
     **/
    @CrossOrigin
    @GetMapping("/api/teacher/getmajorgroup")
    public List<String> findByMajorGroup() {
        return studentUserDAO.getByMajorgroup();
    }

    /**
     * 获取班级信息
     **/
    @CrossOrigin
    @GetMapping("/api/teacher/getbanjigroup")
    public List<String> findByBanjiGroup() {
        return studentUserDAO.getByBanjigroup();
    }


    /**
     * 对课程信息进行分动态查询
     **/
    //分页查询
    @Autowired
    CurriMapper curriDAO;
    @Autowired
    CurriService curriService;

    @CrossOrigin
    @ResponseBody
    @PostMapping("/api/teacher/currilist")
    public Page<CurriModel> pageQuery2(@RequestBody Map<String, Object> json) {
        Integer pageNum = Integer.valueOf((Integer) json.get("pageNum"));
        Integer pageSize = Integer.valueOf((Integer) json.get("pageSize"));
        Integer kechengteacher = Integer.valueOf((String) json.get("kechengteacher"));
        String kechengleibie = String.valueOf((String) json.get("kechengleibie"));
        String like = String.valueOf((String) json.get("query"));

        return curriService.getCurriList(kechengteacher, like, kechengleibie, pageNum, pageSize);
    }


    /**
     * 根据课程ID进行查询
     **/
    @CrossOrigin
    @GetMapping("/api/teacher/getcurri/{kechengid}")
    public CurriModel getCurriInfo(@PathVariable("kechengid") Integer kechengid) {
        Optional<CurriModel> optional = Optional.ofNullable(curriDAO.findByKechengid(kechengid));
        return optional.orElseGet(CurriModel::new);
    }

    /**
     * 获取所有上课教室信息
     **/
    @Autowired
    ClassMapper classDAO;

    @CrossOrigin
    @GetMapping("/api/teacher/getclassroomall")
    public List<String> getClassroomAll() {
        return classDAO.getByClassroom();
    }


    /***
     * 根据课程ID (kechengid) 进行更新
     * **/
    @CrossOrigin
    @PutMapping("/api/teacher/updatecurri/{kechengid}")
    public CurriModel updateCurri(@PathVariable("kechengid") Integer kechengid, @RequestBody CurriModel curriModel) {
        curriModel.setKechengid(kechengid);
        // 修改当前课程类型为必修，添加对应班级的选择该课程
        String leixing = curriModel.getKechengleibie();
        if (leixing.equals("必修")) {
            String[] list = curriModel.getOptionalclass().split(" ");
            String sqltext = "";
            for (int i = 0; i < list.length; i++) {
                List<Integer> list1 = studentUserDAO.findByXhinBJ(list[i]);
                for (int j = 0; j < list1.size(); j++) {
                    SelectionModel selectionModel = new SelectionModel();
                    selectionModel.setKechengid(kechengid);
                    selectionModel.setXuehao(list1.get(0));
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    selectionModel.setCtime(df.format(new Date()));
                    selectionDAO.insert(selectionModel);
                }
            }
            // 删除表中的重复数据
            selectionDAO.deleteRepeat();
        }
        // 删除已选课程中，已移出的可选班级中的学生选课信息
        selectionDAO.deleteRepeat1(curriModel.getOptionalclass(), kechengid);
        curriDAO.updateById(curriModel);
        return curriModel;
    }

    /**
     * 根据课程ID (kechengid) 进行删除
     **/
    @CrossOrigin
    @DeleteMapping("/api/teacher/deletecurri/{kechengid}")
    public Integer deleteCurri(@PathVariable("kechengid") Integer kechengid) {
        return curriDAO.deleteByKechengid(kechengid);
    }


    /**
     * 添加课程信息之前
     * 对教室的使用情况进行判断，避免同一教室同一时间被使用多次
     **/
    @CrossOrigin
    @ResponseBody
    @PostMapping("/api/admin/classroomcheck_curri")
    public List<CurriModel> classroomCheck(@RequestBody Map<String, Object> json) {
        String classroomname = String.valueOf(json.get("classroom"));
        String classweek = String.valueOf(json.get("classweek"));
        String startweek = String.valueOf(json.get("startweek"));
        String endtweek = String.valueOf(json.get("endweek"));
        String classsection = String.valueOf(json.get("classsection"));
        return curriDAO.classroomcheck_curri(classroomname, classweek, classsection, startweek, endtweek);
    }


    @CrossOrigin
    @ResponseBody
    @PostMapping("/api/admin/classroomcheck2/{classRoomName}")
    public com.alibaba.fastjson.JSONObject classroomCheck2(@PathVariable("classRoomName") String classRoomName) {
        Integer count = curriDAO.classRoomcheckByClassRoomName(classRoomName);
        com.alibaba.fastjson.JSONObject jsonObject = new com.alibaba.fastjson.JSONObject();
        jsonObject.put("code", count);
        return jsonObject;
    }

    @CrossOrigin
    @ResponseBody
    @PostMapping("/api/admin/classroomcheck/{classRoomName}")
    public com.alibaba.fastjson.JSONObject classroomCheck(@PathVariable("classRoomName") String classRoomName) {
        List<Classroom> list = classDAO.findAllRoomsByName(classRoomName);
        com.alibaba.fastjson.JSONObject jsonObject = new com.alibaba.fastjson.JSONObject();
        jsonObject.put("code", 0);
        jsonObject.put("status", 200);
        jsonObject.put("data", list);
        return jsonObject;
    }

    @CrossOrigin
    @ResponseBody
    @PutMapping("/api/admin/updateclassroom")
    public com.alibaba.fastjson.JSONObject updateclassroom(@RequestBody Classroom classroom) {
        Integer count = classDAO.updateById(classroom);
        com.alibaba.fastjson.JSONObject jsonObject = new com.alibaba.fastjson.JSONObject();
        jsonObject.put("code", 200);
        jsonObject.put("data", count);
        return jsonObject;
    }



    @CrossOrigin
    @ResponseBody
    @PostMapping("/api/admin/addclassroom")
    public com.alibaba.fastjson.JSONObject classroomCheck(@RequestBody Classroom classroom) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        classroom.setCtime(df.format(new Date()));
        classDAO.insert(classroom);
        com.alibaba.fastjson.JSONObject jsonObject = new com.alibaba.fastjson.JSONObject();
        jsonObject.put("code", 200);
        return jsonObject;
    }

    /**
     * 添加课程信息
     **/
    @CrossOrigin
    @PostMapping("/api/teacher/addcurri")
    public CurriModel saveCurri(@RequestBody CurriModel curriModel) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        curriModel.setCtime(df.format(new Date()));
        curriDAO.insert(curriModel);
        Integer kechengid = curriModel.getKechengid();
        // 修改当前课程类型为必修，添加对应班级的选择该课程
        String leixing = curriModel.getKechengleibie();
        if (leixing.equals("必修")) {
            String[] list = curriModel.getOptionalclass().split(" ");
            String sqltext = "";
            for (int i = 0; i < list.length; i++) {
                List<Integer> list1 = studentUserDAO.findByXhinBJ(list[i]);
                for (int j = 0; j < list1.size(); j++) {
                    SelectionModel selectionModel = new SelectionModel();
                    selectionModel.setKechengid(kechengid);
                    selectionModel.setXuehao(list1.get(j));
                    selectionModel.setCtime(df.format(new Date()));
                    selectionDAO.insert(selectionModel);
                }
            }
        }
        return curriModel;
    }


    /**
     * 获取选课信息 （有对课程名称进行模糊查询和对课程类型进行筛选条件）
     ***/
    @Autowired
    SelectionMapper selectionDAO;

    @CrossOrigin
    @ResponseBody
    @PostMapping("/api/teacher/getselection")
    public List<Map<String, Object>> SelectionQuery(@RequestBody Map<String, Object> json) {
        String kechengteacher = String.valueOf(json.get("kechengteacher"));
        String kechengname = String.valueOf(json.get("kechengname"));
        String kechengleibie = String.valueOf(json.get("kechengleibie"));


        List<Map<String, Object>> list = null;
        // 情况一：没有筛选条件的情况
        if (StringUtils.isEmpty(kechengname) && StringUtils.isEmpty(kechengleibie)) {
            list = selectionDAO.SelectionClass(kechengteacher);
        }
        // 情况二：筛选课程类型为必修，模糊查询没有
        if (StringUtils.isEmpty(kechengname) && kechengleibie.equals("必修")) {
            list = selectionDAO.SelectionClassBixiu(kechengteacher);
        }
        // 情况三：筛选课程类型为选修，模糊查询没有
        if (StringUtils.isEmpty(kechengname) && kechengleibie.equals("选修")) {
            list = selectionDAO.SelectionClassXuanxiu(kechengteacher);
        }
        // 情况四：有模糊查询条件，无课程类别筛选
        if (!StringUtils.isEmpty(kechengname) && StringUtils.isEmpty(kechengleibie)) {
            list = selectionDAO.SelectionClassLike(kechengteacher, kechengname);
        }
        // 情况五：有模糊查询条件，课程类别筛选为 必修
        if (!StringUtils.isEmpty(kechengname) && kechengleibie.equals("必修")) {
            list = selectionDAO.SelectionClassLikeBiXiu(kechengteacher, kechengname);
        }
        // 情况五：有模糊查询条件，课程类别筛选为 选修
        if (!StringUtils.isEmpty(kechengname) && kechengleibie.equals("选修")) {
            list = selectionDAO.SelectionClassLikeXuanXiu(kechengteacher, kechengname);
        }
        //a.ctime,a.kechengid,a.kechengleibie,a.kechengname,(select count(b.xuehao) from selection_table as b where a.kechengid=b.kechengid) as 'count'
        String[] columnArray = new String[]{"ctime", "kechengid", "kechengleibie", "kechengname", "count"};
        return list;
    }


    /***
     *获取当前课程的所有选课学生
     * **/
    @CrossOrigin
    @GetMapping("/api/teacher/getallselection/{kechengid}")
    public List<Map<String, Object>> getAllSelection(@PathVariable("kechengid") Integer kechengid) {
        //b.ctime,a.name,a.xuehao,a.sex,a.college,a.major,a.banji
        String[] columnArray = new String[]{"ctime", "name", "xuehao", "sex", "college", "major", "banji"};
        return studentUserDAO.getAllSelection(kechengid);
    }


    /**
     * 根据教师账户获取该教师所有信息
     **/
    @Autowired
    TeacherUserMapper teacherUserDAO;

    @CrossOrigin
    @GetMapping("/api/teacher/getteacherself/{zhanghu}")
    public TeacherUser getSelfTeacher(@PathVariable("zhanghu") Integer zhanghu) {
        return teacherUserDAO.findByZhanghu(zhanghu);
    }

    /**
     * 根据账户信息进行基本信息更新
     **/
    @CrossOrigin
    @PutMapping("/api/teacher/updateself/{zhanghu}")
    public TeacherUser updateTeacherSelf(@PathVariable("zhanghu") Integer zhanghu, @RequestBody TeacherUser teacherUser) {
        teacherUser.setZhanghu(zhanghu);
        teacherUserDAO.updateById(teacherUser);
        return teacherUser;
    }

    /**
     * 根据账户信息对密码进行更新
     **/
    @CrossOrigin
    @PutMapping("/api/teacher/updateselfpwd/{zhanghu}")
    public TeacherUser updateTeacherPwd(@PathVariable("zhanghu") Integer zhanghu, @RequestBody TeacherUser teacherUser) throws Exception {
        teacherUser.setZhanghu(zhanghu);
        teacherUser.setPassword(MD5.md5(teacherUser.getPassword()));
        teacherUserDAO.updateById(teacherUser);
        return teacherUser;
    }


    /**
     * 获取所有学院信息
     **/
    @Autowired
    CollegeMapper collegeDAO;

    @CrossOrigin
    @GetMapping("/api/teacher/getcollege")
    public List<CollegeModel> getCollege() {
        return collegeDAO.findAll();
    }


    /**
     * 根据学院名称获取所有该学院下的专业信息
     **/
    @Autowired
    MajorMapper majorDAO;

    @CrossOrigin
    @PostMapping("/api/teacher/getmajor/{collegename}")
    public List<Map<String, Object>> getMajorByCollegeName(@PathVariable("collegename") String collegename) {
        //m.majorid,m.majorname
        String[] columnArray = new String[]{"majorid", "majorname"};
        return majorDAO.getMajorByCollegename(collegename);
    }

    /**
     * 根据专业名称 获取该专业下的所有班级信息
     **/
    @Autowired
    BanjiMapper banjiDAO;

    @CrossOrigin
    @PostMapping("/api/teacher/getbanji/{majorname}")
    public List<Map<String, Object>> getBanjiByMajorname(@PathVariable("majorname") String majorname) {
        //b.banjiid,b.banjiname
        String[] columnArray = new String[]{"banjiid", "banjiname"};
        return banjiDAO.getBanjiByMajorname(majorname);
    }


    /**
     * 根据当前教师账户所属专业，查询该专业下的所有班级，用于指定该课程的可选班级
     **/
    @CrossOrigin
    @GetMapping("/api/teacher/getbanjibyzh/{zhanghu}")
    public List<BanjiModel> getBanjiByZhanghuu(@PathVariable("zhanghu") Integer zhanghu) {
        return banjiDAO.findByZhanghu(zhanghu);
    }


    /**
     * 根据当前登录的教师账号，获取该教师开设的所有课程以及每门课程的选课人数
     ***/
    @CrossOrigin
    @PostMapping("/api/teacher/getselectionnum/{zhanghu}")
    public List<Map<String, Object>> getSelectionNum(@PathVariable("zhanghu") Integer zhanghu) {
        //c.kechengname,(select COUNT(*) from selection_table as s where s.kechengid=c.kechengid) as '选课人数'
        String[] columnArray = new String[]{"kechengname", "选课人数"};
        return curriDAO.getSelectionNum(zhanghu);
    }


    /**
     * 教师找回密码操作
     **/

    @CrossOrigin
    @ResponseBody
    @PostMapping(value = "/api/teacher/forgetepwd", produces = "application/json;charset=UTF-8")
    public Object sendEmail(@RequestBody Map<String, Object> json) {
        String qq = String.valueOf(json.get("email"));
        Integer zhanghu = Integer.valueOf(String.valueOf(json.get("zhanghao")));
        String mibao = String.valueOf(json.get("mibao"));
//        TeacherUser teacherUser = teacherUserDAO.findByZhanghu(zhanghu);
        TeacherUser teacherUser = teacherUserDAO.findByZhanghuAndEmailAndMibao(zhanghu, qq, mibao);
        // 账号不存在
        if (teacherUser == null) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("msg", "账户和邮箱数据有误!");
            jsonObject.put("code", "255");
            return jsonObject.toString();
        } else {
            Object jsonStr = userService.sendEmailHanle("teacher", zhanghu, teacherUser.getName(), teacherUser.getEmail(), qq, 2);
            if (jsonStr.equals("")) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("msg", "邮件发送失败！出现未知错误！");
                jsonObject.put("code", "256");
                return jsonObject.toString();
            }
            return jsonStr;
        }
    }


    /**
     * 邮箱测试访问接口
     * http://localhost:8080/credit/email
     *
     * @return
     */
    @GetMapping(value = "/email", produces = "application/json;charset=UTF-8")
    public Object email() {
        Object jsonStr = userService.sendEmailHanle("teacher", 180022, "七七老师", "737795279@qq.com", "737795279@qq.com", 2);
        System.out.println("email test===========>" + jsonStr);
        return jsonStr;
    }


    /**
     * 根据账户对密码进行重置，用于忘记密码之后找回的时候充值密码
     **/
    @CrossOrigin
    @PostMapping(value = "/api/teacher/resetpwd", produces = "application/json;charset=UTF-8")
    public Object ResetPassword(@RequestBody Map<String, Object> json) throws Exception {
        String zhanghu = String.valueOf(json.get("zhanghu"));
        String password = String.valueOf(json.get("password"));
        TeacherUser teacherUser = teacherUserDAO.findByZhanghu(Integer.valueOf(zhanghu));
        if (teacherUser == null) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("msg", "数据有误 !请检查后重试！");
            jsonObject.put("code", "255");
            return jsonObject.toString();
        } else {
            teacherUser.setPassword(MD5.md5(password));
            teacherUserDAO.updateById(teacherUser);
            if (teacherUser == null) {
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


}
