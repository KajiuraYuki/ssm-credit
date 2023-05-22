package com.utils;

import java.util.Random;

/**
 * @Classname Code
 * @Description 获取验证码(用于短信验证以及密码找回模块链接验证)
 * @Date 2021/3/1 16:01
 * @Created by SHTL
 */
public class Code {
    private String codes = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRESTUVWXYZ0123456789";
    Random random = new Random();

    private String randomChar() {
        int index = random.nextInt(codes.length());
        //返回随机字符
        return codes.charAt(index) + "";
    }

    public String getCode() {
        String codeText = "";
        for (int i = 0; i < 4; i++) {
            codeText += randomChar();
        }
        return codeText;
    }

}
