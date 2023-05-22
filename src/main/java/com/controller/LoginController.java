package com.controller;

import com.entity.AdminUser;
import com.entity.StudentUser;
import com.entity.TeacherUser;
import com.mapper.AdminUserMapper;
import com.mapper.StudentUserMapper;
import com.mapper.TeacherUserMapper;
import com.utils.ResultCode;
import com.service.TokenService;
import com.service.UserService;

import com.utils.MD5;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;



@RestController
public class LoginController {

    /**
     * 管理员账户登录接口
     **/
    @Autowired
    UserService userService;
    @Autowired
    TokenService tokenService;
    @Autowired
    AdminUserMapper adminUserDAO;

    @CrossOrigin
    @PostMapping(value = "/api/login_admin", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Object login_admin(@RequestBody AdminUser requestUser) throws Exception {
        Integer username = requestUser.getZhanghao();
        AdminUser adminUser = adminUserDAO.findByZhanghao(username);
        if (adminUser == null) {
            ResultCode result = new ResultCode(255);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("msg", "用户不存在!");
            jsonObject.put("code", "255");
            return jsonObject.toString();
        } else {
            if (MD5.verify(requestUser.getPassword(), adminUser.getPassword())) {
                String token = tokenService.getAdminToken(requestUser);
                ResultCode result = new ResultCode(200);
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("msg", "登录成功！");
                jsonObject.put("code", "200");
                jsonObject.put("token", token);
                jsonObject.put("adminname", adminUser.getName());
                return jsonObject.toString();
            } else {
                ResultCode result = new ResultCode(256);
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("msg", "密码错误！");
                jsonObject.put("code", "256");
                return jsonObject.toString();
            }
        }
    }

    /**
     * 教师账户登录接口
     ***/
    @Autowired
    TeacherUserMapper teacherUserDAO;

    @CrossOrigin
    @PostMapping(value = "/api/login_teacher", produces = "application/json;charset=UTF-8")
    @ResponseBody
    // 教师账户的登录 api
    public Object login_teacher(@RequestBody TeacherUser requestUser) throws Exception {
        Integer username = requestUser.getZhanghu();
        TeacherUser teacherUser = teacherUserDAO.findByZhanghu(username);
        if (teacherUser == null) {
            ResultCode result = new ResultCode(255);
            // 将获取的json数据封装一层，然后在给返回
            JSONObject jsobj = new JSONObject();
            jsobj.put("msg", "用户不存在！");
            jsobj.put("code", "255");
            return jsobj.toString();
        } else {
            if (MD5.verify(requestUser.getPassword(), teacherUser.getPassword())) {
                String token = tokenService.getTeacherToken(requestUser);
                ResultCode result = new ResultCode(200);
                // 将获取的json数据封装一层，然后在给返回
                JSONObject jsobj = new JSONObject();
                jsobj.put("msg", "登录成功！");
                jsobj.put("code", "200");
                jsobj.put("token", token);
                jsobj.put("teachername", teacherUser.getName());
                return jsobj.toString();
            } else {
                ResultCode result = new ResultCode(256);
                // 将获取的json数据封装一层，然后在给返回
                JSONObject jsobj = new JSONObject();
                jsobj.put("msg", "密码错误！");
                jsobj.put("code", "256");
                return jsobj.toString();
            }
        }
    }


    /**
     * 学生账户登录接口
     ***/
    @Autowired
    StudentUserMapper studentUserDAO;

    @CrossOrigin
    @PostMapping(value = "/api/login_student", produces = "application/json;charset=UTF-8")
    @ResponseBody
    // 学生用户的登录 api
    public Object login_student(@RequestBody StudentUser requestUser) throws Exception {
        Integer username = requestUser.getXuehao();
        StudentUser studentUser = studentUserDAO.findByXuehao(username);
        if (studentUser == null) {
            ResultCode result = new ResultCode(255);
            // 将获取的json数据封装一层，然后在给返回
            JSONObject jsobj = new JSONObject();
            jsobj.put("msg", "用户不存在！");
            jsobj.put("code", "255");
            return jsobj.toString();
        } else {
            if (MD5.verify(requestUser.getPassword(), studentUser.getPassword())) {
                String token = tokenService.getStudentToken(requestUser);
                ResultCode result = new ResultCode(200);
                // 将获取的json数据封装一层，然后在给返回
                JSONObject jsobj = new JSONObject();
                jsobj.put("msg", "登录成功！");
                jsobj.put("code", "200");
                jsobj.put("token", token);
                jsobj.put("studentname", studentUser.getName());
                return jsobj.toString();
            } else {
                ResultCode result = new ResultCode(256);
                // 将获取的json数据封装一层，然后在给返回
                JSONObject jsobj = new JSONObject();
                jsobj.put("msg", "密码错误！");
                jsobj.put("code", "256");
                return jsobj.toString();
            }
        }
    }


}
