package com.utils;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 转化数据工具类
 *
 * @author
 * @data 2023/05/01
 */
public class TransferDataUtil {

    private final static Logger log = LoggerFactory.getLogger(TransferDataUtil.class);

    /**
     * 将传过来的对象转化成json对象
     *
     * @param resultList
     * @return
     */
    public static String returnJsonStr(Object resultList) {
        log.info("returnJsonStr.....");
        return JSONObject.toJSONString(resultList, SerializerFeature.BrowserCompatible);
    }


    /**
     * 数据库的数组对象转化成普通对象
     *
     * @param list
     * @param columnArray
     * @return
     */
    public static List<Object> dbDataObjectArrayToListBean(List<Map<String, Object>> list, String[] columnArray) {
        log.info("dbDataObjectArrayToListBean.....");
        List<Object> resultList = new ArrayList<>();
        //这个Map一定要放在循环里面，如果放在循环外面，你add进去的map指向的是同一个内存地址，不管你add进去多少次，数据都是一样的。
        if (list != null) {
            for (Object obj : list) {
                Map<String, Object> map = new HashMap<>();
                Object[] arr = (Object[]) obj;
                for (int i = 0; i < columnArray.length; i++) {
                    map.put(columnArray[i], arr[i]);
                }
                resultList.add(map);
            }
        }
        return resultList;
    }


    /**
     * db的List<Map<String, Object>> 转化成json字符串
     *
     * @param list
     * @param columnArray 对应数据库的列名
     * @return
     */
    public static String dbListMapToJsonStr(List<Map<String, Object>> list, String[] columnArray) {
        log.info("dbDataObjectArrayToJsonStr.....");
        if (list == null
                || list.size() == 0
                || columnArray == null
                || columnArray.length == 0) {
            log.warn("暂无数据返回空数组.....");
            return "[]";
        }
        //数据库的数组对象转化成Bean
        List<Object> objectList = dbDataObjectArrayToListBean(list, columnArray);
        return returnJsonStr(objectList);
    }

}
