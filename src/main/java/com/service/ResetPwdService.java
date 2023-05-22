package com.service;

import com.entity.ResetPwdModel;
import com.mapper.ResetPwdMapper;
import com.utils.Code;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Classname ResetPwd
 * @Description 重置密码相关方法
 */
@Service("ResetPwdService")
public class ResetPwdService {

    Code code;
    @Autowired
    ResetPwdMapper resetPwdDAO;

    /**
     * (重置密码邮件发送之前，所有验证通过)
     * 如果存在用户信息生成新的验证码，以及新的有效时间对原来的数据进行覆盖
     * 如果是第一次进行密码重置(即重置密码信息表中没有该用户信息)则将数据信息存入到重置密码信息表中
     **/
    public ResetPwdModel ResetModelDate(Integer zhanghu, int type) {
        code = new Code();
        ResetPwdModel resetPwdModel = resetPwdDAO.findByZhanghuAndType(zhanghu, type);
        boolean flag = false;
        if (resetPwdModel == null) {
            resetPwdModel = new ResetPwdModel();
            resetPwdModel.setZhanghu(zhanghu);
            resetPwdModel.setType(type);
            flag = true;
        }
        resetPwdModel.setCode(code.getCode());
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        resetPwdModel.setS_time(df.format(new Date()));
        resetPwdModel.setState_code(1);
        if (flag) {
            resetPwdDAO.insert(resetPwdModel);
        } else {
            resetPwdDAO.updateById(resetPwdModel);
        }
        return resetPwdModel;
    }


    /**
     * 在这里又写这个方法是为了在TimerTask中好调用 ResetPwdDAO 中的 findZhanghuAndType() 这个方法
     * **/
    public ResetPwdModel getZhanghuAndType(Integer zhanghu, Integer type) {
        return resetPwdDAO.findByZhanghuAndType(zhanghu, type);
    }

    /**
     * 在这里又写这个方法是为了在TimerTask中好调用 ResetPwdDAO 中的 saveAndFlush() 这个方法
     * 主要是为了刷新 ResetPwdModel 重置密码信息表中的 数据
     * **/
    public ResetPwdModel saveAndFlush(ResetPwdModel resetPwdModel) {
        if (resetPwdModel.getZhanghu() == null || resetPwdModel.getZhanghu() == 0) {
            resetPwdDAO.insert(resetPwdModel);
        } else {
            resetPwdDAO.updateById(resetPwdModel);
        }
        return resetPwdModel;
    }

}
