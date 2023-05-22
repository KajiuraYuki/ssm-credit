package com.controller;

import com.alibaba.fastjson.JSON;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.entity.*;
import com.mapper.*;
import com.service.UserService;
import com.utils.MD5;
import com.utils.ResultCode;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;

import javax.mail.internet.MimeMessage;
import java.text.SimpleDateFormat;
import java.util.*;


@RestController
public class AdminCotroller {

    /**
     * 获取当前账户的 管理员个人信息
     **/
    @Autowired
    AdminUserMapper adminUserDAO;

    @CrossOrigin
    @GetMapping("/api/admin/getself/{zhanghao}")
    public AdminUser getAdminSelf(@PathVariable("zhanghao") Integer zhanghao) {
        Optional<AdminUser> optional = Optional.ofNullable(adminUserDAO.findByZhanghao(zhanghao));
        return optional.orElseGet(AdminUser::new);
    }


    /**
     * 根据账户信息进行基本信息更新
     **/
    @CrossOrigin
    @PutMapping("/api/admin/updateself/{zhanghao}")
    public AdminUser updateAdminSelf(@PathVariable("zhanghao") Integer zhanghao, @RequestBody AdminUser adminUser) {
        adminUser.setZhanghao(Integer.valueOf(zhanghao));
        adminUserDAO.updateById(adminUser);
        return adminUser;
    }


    /**
     * 根据账户信息对密码进行更新
     **/
    @CrossOrigin
    @PutMapping("/api/admin/updateselfpwd/{zhanghao}")
    public AdminUser updateAdminPwd(@PathVariable("zhanghao") Integer zhanghao, @RequestBody AdminUser adminUser) throws Exception {
        adminUser.setZhanghao(zhanghao);
        adminUser.setPassword(MD5.md5(adminUser.getPassword()));
        adminUserDAO.updateById(adminUser);
        return adminUser;
    }


    /**
     * 添加教师用户信息
     **/
    @Autowired
    TeacherUserMapper teacherUserDAO;
    @Autowired
    CollegeMapper collegeDAO;

    @CrossOrigin
    @PostMapping("/api/admin/addtea")
    public TeacherUser saveTea(@RequestBody TeacherUser teacherUser) throws Exception {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        teacherUser.setCtime(df.format(new Date()));
        teacherUser.setPassword(MD5.md5(teacherUser.getPassword()));
        teacherUserDAO.insert(teacherUser);
        return teacherUser;
    }


    /**
     * 对所有教师信息进行动态分页查询
     **/
    @Autowired
    UserService userService;

    @CrossOrigin
    @ResponseBody
    @PostMapping("/api/admin/tealist")
    public Page<TeacherUser> pageQuery(@RequestBody Map<String, Object> json) {
        Integer pageNum = Integer.valueOf((Integer) json.get("pageNum"));
        Integer pageSize = Integer.valueOf((Integer) json.get("pageSize"));
        String college = String.valueOf((String) json.get("college"));
        String major = String.valueOf(json.get("major"));
        String like = String.valueOf(json.get("query"));
        String sex = String.valueOf(json.get("sex"));
        return userService.getTeaList(like, college, major, sex, pageNum,pageSize );
    }


    /**
     * 根据学号进行删除
     **/
    @Autowired
    StudentUserMapper studentUserDAO;

    @CrossOrigin
    @DeleteMapping("/api/admin/deletestu/{xuehao}")
    public Integer deleteStu(@PathVariable("xuehao") Integer xuehao) {
        return studentUserDAO.deleteByXuehao(xuehao);
    }


    /**
     * 添加学生用户信息
     **/
    @Autowired
    MajorMapper majorDAO;

    @CrossOrigin
    @PostMapping("/api/admin/addstu")
    public StudentUser saveStu(@RequestBody StudentUser studentUser) throws Exception {
        // 设置创建时间
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        studentUser.setCtime(df.format(new Date()));
        studentUser.setPassword(MD5.md5(studentUser.getPassword()));
        studentUserDAO.insert(studentUser);
        return studentUser;
    }


    /**
     * 根据教师账号进行更新
     ***/
    @CrossOrigin
    @PutMapping("/api/admin/updatetea/{zhanghu}")
    public TeacherUser updateTeaUser(@PathVariable("zhanghu") Integer zhanghu, @RequestBody TeacherUser teacherUser) throws Exception {
        teacherUser.setZhanghu(zhanghu);
        if (teacherUser.getPassword().length() != 64) {
            teacherUser.setPassword(MD5.md5(teacherUser.getPassword()));
        }
        teacherUserDAO.updateById(teacherUser);
        return teacherUser;
    }

    /**
     * 根据教师账户进行删除
     **/
    @CrossOrigin
    @DeleteMapping("/api/admin/deletetea/{zhanghu}")
    public Integer deleteTea(@PathVariable("zhanghu") Integer zhanghu) {
        return teacherUserDAO.deleteByZhanghu(zhanghu);
    }


    /**
     * 部门管理，查询所有班级信息
     * 查询条件为：like 学院  专业
     **/
    @Autowired
    BanjiMapper banjiDAO;

    @CrossOrigin
    @ResponseBody
    @PostMapping("/api/admin/getbjinfo")
    public List<Map<String, Object>> BanjiQuery(@RequestBody Map<String, Object> json) {
        String like = String.valueOf(json.get("like"));
        String collegename = String.valueOf(json.get("collegename"));
        String majorname = String.valueOf(json.get("majorname"));

        List<Map<String, Object>> list = null;
        // 情况一： 没有任何筛选条件
        if (StringUtils.isEmpty(collegename) && StringUtils.isEmpty(like) && StringUtils.isEmpty(majorname)) {
            list = banjiDAO.findBJNO();
        }
        // 情况二： like
        if (!StringUtils.isEmpty(like) && StringUtils.isEmpty(collegename) && StringUtils.isEmpty(majorname)) {
            list = banjiDAO.findBJLike(like);
        }
        // 情况三： 学院
        if (!StringUtils.isEmpty(collegename) && StringUtils.isEmpty(majorname) && StringUtils.isEmpty(like)) {
            list = banjiDAO.findBJCollege(collegename);
        }
        // 情况四： 专业
        if (!StringUtils.isEmpty(majorname) && StringUtils.isEmpty(like) && StringUtils.isEmpty(collegename)) {
            list = banjiDAO.findBJMajor(majorname);
        }
        // 情况五： like 和 学院
        if (!StringUtils.isEmpty(collegename) && !StringUtils.isEmpty(like) && StringUtils.isEmpty(majorname)) {
            list = banjiDAO.findBJLikeAndCollege(like, collegename);
        }
        // 情况六：like 和 专业
        if (!StringUtils.isEmpty(majorname) && !StringUtils.isEmpty(like) && StringUtils.isEmpty(collegename)) {
            list = banjiDAO.findBJLikeAndMajor(like, majorname);
        }
        // 情况七： 学院 和 专业
        if (!StringUtils.isEmpty(collegename) && !StringUtils.isEmpty(majorname) && StringUtils.isEmpty(like)) {
            list = banjiDAO.findBJCollegeAndMajor(collegename, majorname);
        }
        // 情况八： like 学院 专业 全都有
        if (!StringUtils.isEmpty(like) && !StringUtils.isEmpty(majorname) && !StringUtils.isEmpty(collegename)) {
            list = banjiDAO.findBJLAll(like, collegename, majorname);
        }

        //b.ctime,b.banjiid,b.banjiname,m.majorid,m.majorname,c.collegeid,c.collegename
        //String[] columnArray = new String[]{"ctime", "banjiid", "banjiname", "majorid", "majorname", "collegeid", "collegename"};
        return list;
    }


    /**
     * 根据班级 ID删除班级 信息
     **/
    @CrossOrigin
    @DeleteMapping("/api/admin/deletebanji/{banjiid}")
    public Integer deleteBanji(@PathVariable("banjiid") Integer banjiid) {
        return banjiDAO.deleteByBanjiid(banjiid);
    }



    /**
     * 根据教室名字删除教室 信息
     **/
    @CrossOrigin
    @DeleteMapping("/api/admin/deleteclassroom/{classroom}")
    public Integer deleteBanji(@PathVariable("classroom") String classroom) {
        EntityWrapper<Classroom> wrapper = new EntityWrapper<>();
        wrapper.eq("classroom", classroom);
        return classDAO.delete(wrapper);
    }

    /**
     * 获取教师信息管理页面学院信息
     **/
    @CrossOrigin
    @GetMapping("/api/admin/getcollegegroup")
    public List<String> findByCollegeGroup() {
        return teacherUserDAO.getByCollegegroupJjs();
    }


    /**
     * 获取教师信息管理页面专业信息
     **/
    @CrossOrigin
    @GetMapping("/api/admin/getmajorgroup")
    public List<String> findByMajorGroup() {
        return teacherUserDAO.getByMajorgroupjs();
    }


    /***
     * 根据 banjiid 获取当前班级的详细信息
     * **/
    @CrossOrigin
    @GetMapping("/api/admin/getbjbybjid/{banjiid}")
    public Map<String, Object> findByBJByBJid(@PathVariable("banjiid") Integer banjiid) {
        return banjiDAO.findByBanjiidA(banjiid);
    }


    /**
     * 根据 banjiid 更新当前班级信息
     **/
    @CrossOrigin
    @PutMapping("/api/admin/updatebanji/{banjiid}")
    public BanjiModel updateBanjiUser(@PathVariable("banjiid") Integer banjiid, @RequestBody Map<String, Object> json) {
        String banjiname = (String) json.get("banjiname");
        String majorname = (String) json.get("majorname");
        String content = (String) json.get("content");
        // 获取 已知majorname 的 majorid
        MajorModel majorModel = majorDAO.findByMajorname(majorname);
        Integer majorid = majorModel.getMajorid();
        BanjiModel banjiModel = new BanjiModel();
        banjiModel.setBanjiid(banjiid);
        banjiModel.setBanjiname(banjiname);
        banjiModel.setMajorid(majorid);
        banjiModel.setContent(content);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        banjiModel.setCtime(df.format(new Date()));
        banjiDAO.updateById(banjiModel);
        return banjiModel;
    }


    /**
     * 添加班级信息
     **/
    @CrossOrigin
    @PostMapping("/api/admin/addbanji")
    public BanjiModel addBanjiUser(@RequestBody Map<String, Object> json) {
        String banjiname = (String) json.get("banjiname");
        String majorname = (String) json.get("majorname");
        String content = (String) json.get("content");
        // 获取 已知majorname 的 majorid
        MajorModel majorModel = majorDAO.findByMajorname(majorname);
        BanjiModel banjiModel = new BanjiModel();
        banjiModel.setBanjiname(banjiname);
        banjiModel.setMajorid(majorModel.getMajorid());
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        banjiModel.setCtime(df.format(new Date()));
        banjiModel.setContent(content);
        banjiDAO.insert(banjiModel);
        return banjiModel;
    }


    /**
     * 添加专业信息
     **/
    @CrossOrigin
    @PostMapping("/api/admin/addmajor")
    public MajorModel addMajorUser(@RequestBody Map<String, Object> json) {
        String majorname = String.valueOf(json.get("majorname"));
        String collegename = String.valueOf(json.get("college"));
        String content = String.valueOf(json.get("content"));
        // 获取已知 collegename 的 collegeid
        CollegeModel collegeModel = collegeDAO.findByCollegename(collegename);
        MajorModel majorModel = new MajorModel();
        majorModel.setCollegeid(collegeModel.getCollegeid());
        majorModel.setMajorname(majorname);
        majorModel.setContent(content);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        majorModel.setCtime(df.format(new Date()));
        majorDAO.insert(majorModel);
        return majorModel;
    }


    /**
     * 获取所有学院信息
     **/
    @CrossOrigin
    @ResponseBody
    @PostMapping("/api/admin/getallcollege")
    public List<CollegeModel> findAllCollege(@RequestBody Map<String, Object> json) {
        String like = (String) json.get("query");
        return collegeDAO.findByCollegenameLike("%" + like + "%");
    }


    /**
     * 根据 collegeid 获取当前id 的学院信息系
     **/
    @CrossOrigin
    @ResponseBody
    @PostMapping("/api/admin/getcollegeinfo/{collegeid}")
    public CollegeModel findCollegeByCid(@PathVariable("collegeid") Integer collegeid) {
        return collegeDAO.findByCollegeid(collegeid);
    }


    /**
     * 根据 collegeid 更新当前学院信息
     **/
    @CrossOrigin
    @PutMapping("/api/admin/updatecollege/{collegeid}")
    public CollegeModel updateCollegeUser(@PathVariable("collegeid") Integer collegeid, @RequestBody Map<String, Object> json) {
        String content = String.valueOf(json.get("content"));
        String collegename = String.valueOf(json.get("collegename"));
        CollegeModel collegeModel = new CollegeModel();
        collegeModel.setCollegeid(collegeid);
        collegeModel.setCollegename(collegename);
        collegeModel.setContent(content);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        collegeModel.setCtime(df.format(new Date()));
        collegeDAO.updateById(collegeModel);
        return collegeModel;
    }


    /**
     * 添加学院信息
     **/
    @CrossOrigin
    @PostMapping("/api/admin/addcollege")
    public CollegeModel addCollegeUser(@RequestBody Map<String, Object> json) {
        String collegename = String.valueOf(json.get("collegename"));
        String content = String.valueOf(json.get("content"));
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        CollegeModel collegeModel = new CollegeModel();
        collegeModel.setContent(content);
        collegeModel.setCollegename(collegename);
        collegeModel.setCtime(df.format(new Date()));
        collegeDAO.insert(collegeModel);
        return collegeModel;
    }


    /**
     * 根据学院 ID删除学院 信息
     **/
    @CrossOrigin
    @DeleteMapping("/api/admin/deletecollege/{collegeid}")
    public Integer deleteCollege(@PathVariable("collegeid") Integer collegeid) {
        return collegeDAO.deleteByCollegeid(collegeid);
    }


    /**
     * 根据专业 ID删除专业 信息
     **/
    @CrossOrigin
    @DeleteMapping("/api/admin/deletemajor/{majorid}")
    public Integer deleteMajor(@PathVariable("majorid") Integer majorid) {
        return majorDAO.deleteByMajorid(majorid);
    }


    /**
     * 在 专业表中 获取 学院的个数，用于条件查询
     **/
    @CrossOrigin
    @GetMapping("/api/admin/getcollegeinmajor")
    public List<Object> findCollegeInMajor() {
        return majorDAO.getCollegeInMajor();
    }


    /**
     * 获取所有专业信息,用于专业管理页面
     **/
    @CrossOrigin
    @ResponseBody
    @PostMapping("/api/admin/getallmajor")
    public List<Map<String, Object>> findAllMajor(@RequestBody Map<String, Object> json) {
        String like = String.valueOf(json.get("like"));
        String collegename = String.valueOf(json.get("collegename"));

        List<Map<String, Object>> list = null;
        // 1. 没有查询条件
        if (StringUtils.isEmpty(like) && StringUtils.isEmpty(collegename)) {
            try {
                list = majorDAO.findMJNO();
                System.out.println(JSON.toJSONString(list));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        // 2. 只有like
        if (!StringUtils.isEmpty(like) && StringUtils.isEmpty(collegename)) {
            list = majorDAO.findMJLike(like);
        }
        // 3. 只有 colleg
        if (StringUtils.isEmpty(like) && !StringUtils.isEmpty(collegename)) {
            list = majorDAO.findMJCollege(collegename);
        }
        if (!StringUtils.isEmpty(like) && !StringUtils.isEmpty(collegename)) {
            list = majorDAO.findMJLikeAndCollege(like, collegename);
        }

        //m.majorid,m.majorname,m.content,m.ctime,c.collegename
        String[] columnArray = new String[]{"majorid", "majorname", "content", "ctime", "collegename"};
        return list;
    }


    /**
     * 根据当前 majorid 获取当前 的 专业信息
     **/
    @CrossOrigin
    @ResponseBody
    @PostMapping("/api/admin/getmajorinfo/{majorid}")
    public Map<String, Object> findMajorByMid(@PathVariable("majorid") Integer majorid) {
        return majorDAO.findByMJinMid(majorid);
    }


    /**
     * 根据 majorid 更新当前专业信息
     **/
    @CrossOrigin
    @PutMapping("/api/admin/updatemajor/{majorid}")
    public MajorModel updateMajorUser(@PathVariable("majorid") Integer majorid, @RequestBody Map<String, Object> json) {
        String content = String.valueOf(json.get("content"));
        String majorname = String.valueOf(json.get("majorname"));
        String collegeid = String.valueOf(json.get("collegename"));
        int k = 1;
        for (int i = 0; i < collegeid.length(); i++) {
            if (!Character.isDigit(collegeid.charAt(i))) {
                k = 0;
                break;
            }
        }


        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        MajorModel majorModel = new MajorModel();
        majorModel.setMajorid(majorid);
        majorModel.setMajorname(majorname);
        majorModel.setContent(content);
        CollegeModel collegeModel;
        // 是数字
        if (k == 1) {
            // 获取当前collegeid 的 collegename
            collegeModel = collegeDAO.findByCollegeid(Integer.valueOf(collegeid));
        } else {
            // 不是数字
            collegeModel = collegeDAO.findByCollegename(collegeid);
        }
        majorModel.setCollegeid(collegeModel.getCollegeid());
        majorModel.setCtime(df.format(new Date()));
        majorDAO.updateById(majorModel);
        return majorModel;
    }


    /**
     * 获取所有教室信息，，仅一个 like
     **/
    @Autowired
    ClassMapper classDAO;

    @CrossOrigin
    @ResponseBody
    @PostMapping("/api/admin/getallrooms")
    public List<Classroom> findAllRooms(@RequestBody Map<String, Object> json) {
        String like = String.valueOf(json.get("like"));
        return classDAO.findAllRooms(like);
    }


    @CrossOrigin
    @ResponseBody
    @PostMapping("/api/admin/getallcr")
    public List<Classroom> getallcr(@RequestBody Map<String, Object> json) {
        String query = String.valueOf(json.get("query"));
        if (StringUtils.isEmpty(query)) {
            return classDAO.findAllRooms2();
        } else {
            return classDAO.findAllRooms(query);
        }

    }


    /**
     * 获取每个学院的学生和教师人数数量
     **/
    @CrossOrigin
    @ResponseBody
    @GetMapping(value = "/api/admin/getnums")
    public List<Map<String, Object>> getAllNums() {
        List<Map<String, Object>> list = collegeDAO.getAllNums();
        return list;
    }


    /**
     * 管理员找回密码操作
     **/
    // 注入QQ发送邮件的bean
    @Autowired
    JavaMailSender mailSender;

    @CrossOrigin
    @ResponseBody
    @PostMapping(value = "/api/admin/forgetepwd", produces = "application/json;charset=UTF-8")
    public Object sendEmail(@RequestBody Map<String, Object> json) {
        String qq = String.valueOf(json.get("email"));
        Integer zhanghu = Integer.valueOf(String.valueOf(json.get("zhanghao")));
        String mibao = String.valueOf(json.get("mibao"));
        AdminUser adminUser = adminUserDAO.findByZhanghaoAndEmailAndMibao(zhanghu, qq, mibao);
        // 账号不存在
        if (adminUser == null) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("msg", "账户和邮箱数据有误!");
            jsonObject.put("code", "255");
            return jsonObject.toString();
        } else {
            Object jsonStr = userService.sendEmailHanle("admin", zhanghu, adminUser.getName(), adminUser.getEmail(), qq, 1);
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
     * 根据账户对密码进行重置，用于忘记密码之后找回的时候充值密码
     **/
    @CrossOrigin
    @PostMapping("/api/admin/resetpwd")
    public Object ResetPassword(@RequestBody Map<String, Object> json) throws Exception {
        String zhanghu = String.valueOf(json.get("zhanghu"));
        String email = String.valueOf(json.get("email"));
        String password = String.valueOf(json.get("password"));
        AdminUser adminUser = adminUserDAO.findByZhanghaoAndEmail(Integer.valueOf(zhanghu), email);
        if (adminUser == null) {
            ResultCode result = new ResultCode(255);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("msg", "账户和邮箱数据有误 !");
            jsonObject.put("code", "255");
            return jsonObject.toString();
        } else {
            adminUser.setPassword(MD5.md5(password));
            adminUserDAO.updateById(adminUser);
            if (adminUser == null) {
                ResultCode result = new ResultCode(255);
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("msg", "重置密码失败!");
                jsonObject.put("code", "255");
                return jsonObject.toString();
            } else {
                ResultCode result = new ResultCode(255);
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("msg", "重置密码成功!");
                jsonObject.put("code", "200");
                return jsonObject.toString();
            }
        }
    }


}
