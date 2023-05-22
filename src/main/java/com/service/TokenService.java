package com.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.entity.AdminUser;
import com.entity.StudentUser;
import com.entity.TeacherUser;
import org.springframework.stereotype.Service;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;

/**
 * @Classname TokenService
 * @Description 生成token
 */
@Service("TokenService")
public class TokenService {
    /**
     * 获取管理员端 token
     **/
    public String getAdminToken(AdminUser adminUser) {
        Date start = new Date();
        long currentTime = System.currentTimeMillis() + 60 * 60 * 1000; // 一小时有效时间
        Date end = new Date(currentTime);
        String token = "";

        token = JWT.create().withAudience(String.valueOf(adminUser.getZhanghao())).withIssuedAt(start).withExpiresAt(end)
                .sign(Algorithm.HMAC256(adminUser.getPassword()));
        return token;
    }


    /**
     * 获取教师端 token
     **/
    public String getTeacherToken(TeacherUser teacherUser) {
        Date start = new Date();
        long currentTime = System.currentTimeMillis() + 60 * 60 * 1000; // 一小时有效时间
        Date end = new Date(currentTime);
        String token = "";

        token = JWT.create().withAudience(String.valueOf(teacherUser.getZhanghu())).withIssuedAt(start).withExpiresAt(end)
                .sign(Algorithm.HMAC256(teacherUser.getPassword()));
        return token;
    }

    /**
     * 获取学生端 token
     **/
    public String getStudentToken(StudentUser studentUser) {
        Date start = new Date();
        long currentTime = System.currentTimeMillis() + 60 * 60 * 1000; // 一小时有效时间
        Date end = new Date(currentTime);
        String token = "";

        token = JWT.create().withAudience(studentUser.getXuehao().toString()).withIssuedAt(start).withExpiresAt(end)
                .sign(Algorithm.HMAC256(studentUser.getPassword()));
        return token;
    }
}
