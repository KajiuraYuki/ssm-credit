package com.utils;

import com.entity.ResetPwdModel;
import com.service.ResetPwdService;

import java.util.Timer;
import java.util.TimerTask;

/**
 * @Classname MyTimerClass
 * @Description 让reset_pwd_table 重置密码表中的状态码 state_code 为0 也就是将状态设为过期
 * @Date 2021/3/4 17:14
 * @Created by SHTL
 */
public class MyTimerTask extends TimerTask {

    ResetPwdService resetPwdService = (ResetPwdService) ApplicationContextUtil.getBean("ResetPwdService");
    private Timer timer = null;
    private Integer zhanghu = 0;
    private Integer type = 0;

    public MyTimerTask(Timer timer, Integer zhanghu, Integer type) {
        this.timer = timer;
        this.zhanghu = zhanghu;
        this.type = type;
    }


    @Override
    public void run() {
        try {
            System.out.println("timer run");
            ResetPwdModel resetPwdModel = resetPwdService.getZhanghuAndType(zhanghu, type);
            if (resetPwdModel != null) {
                resetPwdModel.setState_code(0);
                resetPwdService.saveAndFlush(resetPwdModel);
            }

            timer.cancel();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
