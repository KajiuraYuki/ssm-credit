package com.controller;

import com.entity.ResetPwdModel;
import com.mapper.ResetPwdMapper;
import com.service.ResetPwdService;
import com.utils.MyTimerTask;
import com.utils.Code;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Timer;



@RestController
public class ResetPwdController {

    Code getcode = new Code();

    /**
     * (重置密码邮件发送之前，所有验证通过)
     * 如果存在用户信息生成新的验证码，以及新的有效时间对原来的数据进行覆盖
     * 如果是第一次进行密码重置(即重置密码信息表中没有该用户信息)则将数据信息存入到重置密码信息表中
     **/
    @Autowired
    ResetPwdMapper resetPwdDAO;
    ResetPwdService resetPwdService;

    @CrossOrigin
    @PostMapping(value = "/api/resetpwd/addresetpwd", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResetPwdModel addResetpwd(@RequestBody Map<String, Object> json) {
        Timer timer = new Timer();
        Integer zhanghu = Integer.valueOf(String.valueOf(json.get("zhanghu")));
        ResetPwdModel resetPwdModel = resetPwdDAO.findByZhanghu(zhanghu);
        Integer type = 0;
        String typeStr = String.valueOf(json.get("type"));
        switch (typeStr) {
            case "admin":
                type = 1;
                break;
            case "teacher":
                type = 2;
                break;
            case "student":
                type = 3;
                break;
        }
        boolean flag = false;
        if (resetPwdModel == null) {
            resetPwdModel = new ResetPwdModel();
            resetPwdModel.setZhanghu(zhanghu);
            resetPwdModel.setType(type);
            flag = true;
        }
        resetPwdModel.setCode(getcode.getCode());
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        resetPwdModel.setS_time(df.format(new Date()));
        resetPwdModel.setState_code(0);
        if (flag) {
            resetPwdDAO.insert(resetPwdModel);
        } else {
            resetPwdDAO.updateById(resetPwdModel);
        }
        // 创建/更新验证码之后执行一个定时器，实现控制验证码的有效期
        MyTimerTask myTimerTask = new MyTimerTask(timer, zhanghu, type);
        timer.schedule(myTimerTask, 5000);
        return resetPwdModel;
    }


    /**
     * 重置密码完成，将原来的验证码进行覆盖，防止使用原来的验证码进行重置
     **/
    @CrossOrigin
    @PostMapping(value = "/api/resetpwd/clrcode{zhanghu}", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Object clearCode(@PathVariable("zhanghu") Integer zhanghu) {
        ResetPwdModel resetPwdModel = resetPwdDAO.findByZhanghu(zhanghu);
        resetPwdModel.setCode(getcode.getCode());
        resetPwdDAO.updateById(resetPwdModel);
        return resetPwdModel;
    }


    /**
     * 检查当前账户的状态码
     **/
    @CrossOrigin
    @PostMapping(value = "/api/resetpwd/checkstatus", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Object checkStatus(@RequestBody Map<String, Object> json) {
        Integer zhanghu = Integer.valueOf(String.valueOf(json.get("zhanghu")));
        Integer type = Integer.valueOf(String.valueOf(json.get("typevalue")));
        String code = String.valueOf(json.get("code"));
        ResetPwdModel resetPwdModel = resetPwdDAO.findByZhanghuAndTypeAndCode(zhanghu, type, code);
        if (resetPwdModel == null) {
            // 用户不存在，url链接错误
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("msg", "url有误！请输入正确的url地址！");
            jsonObject.put("code", "256");
            return jsonObject.toString();
        } else {
            if (resetPwdModel.getState_code() == 1) {
                JSONObject jsonObject = new JSONObject();
                // 验证码在有效期之内
                jsonObject.put("msg", "url验证通过！");
                jsonObject.put("code", "200");
                return jsonObject.toString();
            } else {
                // 验证码已经失效
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("msg", "该链接已失效！请重新获取链接！");
                jsonObject.put("code", "256");
                return jsonObject.toString();
            }
        }
    }


//    @CrossOrigin
//    @PostMapping(value = "/api/resetpwd/checkstatus")
//    @ResponseBody
//    public Object checkStatus(@RequestBody Map<String, Object> json) {
//        Integer zhanghu = Integer.valueOf(String.valueOf(json.get("zhanghu")));
//        ResetPwdModel resetPwdModel = resetPwdDAO.findByZhanghu(zhanghu);
//        int state_code = resetPwdModel.getState_code();
//        String code = resetPwdModel.getCode();
//        if (state_code == 0) {
//            ResultCode result = new ResultCode(233);
//            // 将获取的json数据封装一层，然后在给返回
//            JSONObject jsobj = new JSONObject();
//            jsobj.put("msg", "当前链接已过期！");
//            jsobj.put("code", "233");
//            return jsobj.toString();
//        } else if (!String.valueOf(json.get("code")).equals(code)) {
//            ResultCode result = new ResultCode(233);
//            // 将获取的json数据封装一层，然后在给返回
//            JSONObject jsobj = new JSONObject();
//            jsobj.put("msg", "当前链接已过期或链接不正确！");
//            jsobj.put("code", "233");
//            return jsobj.toString();
//        } else {
//            ResultCode result = new ResultCode(200);
//            // 将获取的json数据封装一层，然后在给返回
//            JSONObject jsobj = new JSONObject();
//            jsobj.put("msg", "验证通过！");
//            jsobj.put("code", "200");
//            return jsobj.toString();
//        }
//    }


}
