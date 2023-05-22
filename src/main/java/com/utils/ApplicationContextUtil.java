package com.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @Classname ApplicationContextUtil
 * @Description 这个工具类是为了在 TimerTask 中调用 Service 层中的方法
 * 如果不这样 通过 @Autowired 来进行注入是不行的  始终会报空异常
 * @Date 2021/3/5 19:52
 * @Created by SHTL
 */
//@Component
public class ApplicationContextUtil implements ApplicationContextAware {
    private static ApplicationContext applicationContext;

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ApplicationContextUtil.applicationContext = applicationContext;

    }


    public static Object getBean(String beanName) {
        return applicationContext.getBean(beanName);
    }
}
