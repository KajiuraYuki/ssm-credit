package com.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.entity.AdminUser;
import com.entity.ResetPwdModel;
import com.entity.StudentUser;
import com.entity.TeacherUser;
import com.mapper.AdminUserMapper;
import com.mapper.StudentUserMapper;
import com.mapper.TeacherUserMapper;
import com.utils.MyTimerTask;
import net.sf.json.JSONObject;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.apache.commons.lang3.StringUtils;

import javax.mail.Multipart;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.persistence.criteria.*;
import java.util.*;

/**
 * @Classname UserService
 * @Description 用户(管理员 / 教师 / 学生)的部分方式实现
 */

@Service
public class UserService {


    /**
     * 公用方法1
     * 进行密码找回的邮件发送模块，根据一系列的判断执行，返回最终的处理结果
     **/
    @Autowired
    ResetPwdService resetPwdService;
    // 注入QQ发送邮件的bean
    @Autowired
    JavaMailSender mailSender;


    public Object sendEmailHanle(String typevalue, Integer zhanghu, String username, String email, String qq, int type) {
        // 邮箱与账户不匹配
        if (!email.equals(qq)) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("msg", "邮箱与账户不匹配！");
            jsonObject.put("code", "256");
            return jsonObject.toString();
        }
        // 邮箱与账号匹配成功
        if (email.equals(qq)) {
            // 对当前账号数据进行判断,重置密码信息表中创建或更新成功没有
            ResetPwdModel flag = resetPwdService.ResetModelDate(zhanghu, type);
            if (flag == null) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("msg", "数据处理出错！");
                jsonObject.put("code", "256");
                return jsonObject.toString();
            } else {
                String title = "图灵教务系统密码找回";
                try {
                    MimeMessage mimeMessage = this.mailSender.createMimeMessage();
                    MimeMessageHelper message = new MimeMessageHelper(mimeMessage);
                    message.setFrom("shtlls@qq.com");
                    message.setTo(qq);
                    message.setSubject(title);
                    String encodeUrlStr = "typevalue=" + typevalue + "&zhanghu=" + zhanghu + "&code=" + flag.getCode();
                    String encodeUrl = Base64.getUrlEncoder().encodeToString(encodeUrlStr.getBytes("utf-8"));
                    encodeUrl = "http://127.0.0.1:8080/#/reset?" + encodeUrl;
                    MimeBodyPart htmlPart = new MimeBodyPart();
                    String htmlStr = "<div><div style='margin-left:4%;'><p style='color:red;'>" + username + " 您好：</p>" +
                            "<p style='text-indent: 2em;'>您正在使用密码找回功能，请点击下面的链接完成密码找回。</p>" +
                            "<p style='text-indent: 2em;display: block;word-break: break-all;'>" +
                            "链接地址：<a style='text-decoration: none;' href='" + encodeUrl + "'>" + encodeUrl + "</a></p>" +
                            "</div>" +
                            "<ul style='color: rgb(169, 169, 189);font-size: 18px;'>" +
                            "<li>为了保障您帐号的安全，该链接有效期为 5 分钟。</li>" +
                            "<li>请您在有效期内进行密码重置，请周知。</li>" +
                            "<li>如果该链接无法点击，请直接复制以上网址到浏览器地址栏中访问。</li>" +
                            "<li>请您妥善保管，此邮件无需回复。</li>" +
                            "</ul>" +
                            "</div>";
                    htmlPart.setContent(htmlStr, "text/html; charset=utf-8");
                    Multipart mt = new MimeMultipart("alternative");  // alternative 表示纯文本与超文本共存。
                    mt.addBodyPart(htmlPart);
                    mimeMessage.setContent(mt);
                    this.mailSender.send(mimeMessage);
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("msg", "邮件发送成功！");
                    jsonObject.put("code", "200");
                    //　创建Tiemr时间，5分钟之后执行，更新 ResetPwdTable表中的state_code为0，表示链接过期
                    Timer timer = new Timer();
                    timer.schedule(new MyTimerTask(timer, zhanghu, type), 300000);
                    return jsonObject.toString();
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("msg", "邮件发送失败！");
                    jsonObject.put("code", "256");
                    return jsonObject.toString();
                }
            }
        }
        return "";
    }


    /**
     * 管理员端一些方法
     **/
    @Autowired
    AdminUserMapper adminUserDAO;

    // token的生成方法
    public String getAdminToken(AdminUser adminUser) {
        String token = "";
        token = JWT.create().withAudience(String.valueOf(adminUser.getZhanghao())).sign(Algorithm.HMAC256(adminUser.getPassword()));
        return token;
    }


    /**
     * 教师端一些方法
     **/
    @Autowired
    TeacherUserMapper teacherUserDAO;

    // token的生成方法
    public String getTeacherToken(TeacherUser teacherUser) {
        String token = "";
        token = JWT.create().withAudience(String.valueOf(teacherUser.getZhanghu())).sign(Algorithm.HMAC256(teacherUser.getPassword()));
        return token;
    }


    /**
     * 学生端一些方法
     **/
    @Autowired
    StudentUserMapper studentUserDAO;

    // token的生成方法
    public String getStudentToken(StudentUser studentUser) {
        String token = "";
        token = JWT.create().withAudience(studentUser.getXuehao().toString()).sign(Algorithm.HMAC256(studentUser.getPassword()));
        return token;
    }


    // 获取学生信息列表，实现多条件动态查询并分页
    public Page<StudentUser> getStuList(
            String like, String college, String major,
            String banji, String sex, int pageNum, int pageSize) {

        EntityWrapper<StudentUser> entityWrapper = new EntityWrapper<>();
        // like模糊查询
        if (!StringUtils.isEmpty(like)) {
            entityWrapper.like("name", like);
        }
        // 精确查询String类型 college
        if (!StringUtils.isEmpty(college)) {
            entityWrapper.eq("college", college);
        }
        // 精确查询String类型 major
        if (!StringUtils.isEmpty(major)) {
            entityWrapper.eq("major", major);
        }
        // 精确查询String类型 banji
        if (!StringUtils.isEmpty(banji)) {
            entityWrapper.eq("banji", banji);
        }
        // 精确查询String类型 sex
        if (!StringUtils.isEmpty(sex)) {
            entityWrapper.eq("sex", sex);
        }
        // 排序
        entityWrapper.orderAsc(Arrays.asList("xuehao"));

        Page<StudentUser> page = new Page<>(pageNum, pageSize);
        List<StudentUser> studentUserList = studentUserDAO.selectPage(page, entityWrapper);

        page.setCurrent(pageNum);
        page.setSize(pageSize);
        page.setRecords(studentUserList);
        return page;
    }



    /*public Page<StudentUser> getStuList(
            String like, String college, String major,
            String banji, String sex, Pageable pageable) {
        Specification<StudentUser> specification = (Specification<StudentUser>) (root, criteriaQuery, cb) -> {
            // 存放查询条件
            List<Predicate> predicatesList = new ArrayList<>();
            // like模糊查询
            if (!StringUtils.isEmpty(like)) {
                Predicate namePredicate = cb.like(root.get("name"), "%" + like + "%");
                predicatesList.add(namePredicate);
            }
            // 精确查询String类型 college
            if (!StringUtils.isEmpty(college)) {
                Predicate collegePredicate = cb.equal(root.get("college"), college);
                predicatesList.add(collegePredicate);
            }
            // 精确查询String类型 major
            if (!StringUtils.isEmpty(major)) {
                Predicate majorPredicate = cb.equal(root.get("major"), major);
                predicatesList.add(majorPredicate);
            }
            // 精确查询String类型 banji
            if (!StringUtils.isEmpty(banji)) {
                Predicate banjiPredicate = cb.equal(root.get("banji"), banji);
                predicatesList.add(banjiPredicate);
            }
            // 精确查询String类型 sex
            if (!StringUtils.isEmpty(sex)) {
                Predicate sexPredicate = cb.equal(root.get("sex"), sex);
                predicatesList.add(sexPredicate);
            }
            // 排序
            criteriaQuery.orderBy(cb.asc(root.get("xuehao")));
            // 最终将查询条件拼好，然后return
            Predicate[] predicates = new Predicate[predicatesList.size()];
            return cb.and(predicatesList.toArray(predicates));
        };
        Page<StudentUser> page = new PageImpl<>(studentUserDAO.findAll(null));
        return page;
    }*/



    // 获取教师信息列表，实现多条件动态查询并分页
    public Page<TeacherUser> getTeaList(String like, String college, String major,
            String sex, int pageNum, int pageSize) {
        EntityWrapper<TeacherUser> entityWrapper = new EntityWrapper<>();

        // like模糊查询
        if (!StringUtils.isEmpty(like)) {
            entityWrapper.like("name", like);
        }
        // 精确查询String类型 college
        if (!StringUtils.isEmpty(college)) {
            entityWrapper.eq("college", college);
        }
        // 精确查询String类型 major
        if (!StringUtils.isEmpty(major)) {
            entityWrapper.eq("major", major);
        }
        // 精确查询String类型 sex
        if (!StringUtils.isEmpty(sex)) {
            entityWrapper.eq("sex", sex);
        }
        // 排序
        entityWrapper.orderAsc(Arrays.asList("zhanghu"));


        Page<TeacherUser> page = new Page<>(pageNum, pageSize);
        List<TeacherUser> teacherUserList = teacherUserDAO.selectPage(page, entityWrapper);

        page.setCurrent(pageNum);
        page.setSize(pageSize);
        page.setRecords(teacherUserList);
        return page;
    }


    /*public Page<TeacherUser> getTeaList(
            String like, String college, String major,
            String sex, Pageable pageable) {
        Specification<TeacherUser> specification = (Specification<TeacherUser>) (root, criteriaQuery, cb) -> {
            // 存放查询条件
            List<Predicate> predicatesList = new ArrayList<>();
            // like模糊查询
            if (!StringUtils.isEmpty(like)) {
                Predicate namePredicate = cb.like(root.get("name"), "%" + like + "%");
                predicatesList.add(namePredicate);
            }
            // 精确查询String类型 college
            if (!StringUtils.isEmpty(college)) {
                Predicate collegePredicate = cb.equal(root.get("college"), college);
                predicatesList.add(collegePredicate);
            }
            // 精确查询String类型 major
            if (!StringUtils.isEmpty(major)) {
                Predicate majorPredicate = cb.equal(root.get("major"), major);
                predicatesList.add(majorPredicate);
            }
            // 精确查询String类型 sex
            if (!StringUtils.isEmpty(sex)) {
                Predicate sexPredicate = cb.equal(root.get("sex"), sex);
                predicatesList.add(sexPredicate);
            }
            // 排序
            criteriaQuery.orderBy(cb.asc(root.get("zhanghu")));
            // 最终将查询条件拼好，然后return
            Predicate[] predicates = new Predicate[predicatesList.size()];
            return cb.and(predicatesList.toArray(predicates));
        };
        Page<TeacherUser> page = new PageImpl<>(teacherUserDAO.findAll(null));
        return page;
    }*/

}
