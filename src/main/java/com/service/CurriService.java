package com.service;


import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.entity.CurriModel;
import com.mapper.CurriMapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageImpl;
//import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * @Classname CurriService
 * @Description 课程相关方法的具体实现
 */
@Service
public class CurriService {
    @Autowired
    CurriMapper curriMapper;


    // 获取开课信息列表，实现多条件动态查询并分页
    public Page<CurriModel> getCurriList(Integer kechengteacher, String kechengname, String kechengleibie, int pageNo, int pageSize) {


        EntityWrapper<CurriModel> entityWrapper = new EntityWrapper<>();
        // like模糊查询
        if (!StringUtils.isEmpty(kechengname)) {
            entityWrapper.like("kechengname", kechengname);
        }
        // 精确查询String类型 课程类型 kechengleixing
        if (!StringUtils.isEmpty(kechengleibie)) {

            entityWrapper.eq("kechengleibie", kechengleibie);
        }
        // 精确查询String 开课教师 kechengteacher
        if (!StringUtils.isEmpty(kechengteacher.toString())) {
            entityWrapper.eq("kechengteacher", kechengteacher);
        }

        Page<CurriModel> page = new Page<>(pageNo, pageSize);
        List<CurriModel> list = curriMapper.selectPage(page, entityWrapper);
        page.setRecords(list);
        page.setCurrent(pageNo);
        page.setSize(pageSize);
        return page;
    }




    // 获取开课信息列表，实现多条件动态查询并分页
    /*public Page<CurriModel> getCurriList2(Integer kechengteacher, String kechengname, String kechengleibie, Pageable pageable) {


        Specification<CurriModel> specification = (Specification<CurriModel>) (root,criteriaQuery,cb) -> {
            // 存放查询条件
            List<Predicate> predicateList = new ArrayList<>();
            // like模糊查询
            if (!StringUtils.isEmpty(kechengname)) {
                Predicate namePredicate =  cb.like(root.get("kechengname"),"%" + kechengname + "%");
                predicateList.add(namePredicate);
            }
            // 精确查询String类型 课程类型 kechengleixing
            if (!StringUtils.isEmpty(kechengleibie)) {
                Predicate leibiePredicate =  cb.equal(root.get("kechengleibie"),kechengleibie);
                predicateList.add(leibiePredicate);
            }
            // 精确查询String 开课教师 kechengteacher
            if (!StringUtils.isEmpty(kechengteacher.toString())) {
                Predicate teacherIdPredicate =  cb.equal(root.get("kechengteacher"),kechengteacher);
                predicateList.add(teacherIdPredicate);
            }
            // 排序
            criteriaQuery.orderBy(cb.asc(root.get("kechengid")));
            // 最终将查询条件拼好，然后return
            Predicate[] predicates = new Predicate[predicateList.size()];
            return cb.and(predicateList.toArray(predicates));

        };
        Page<CurriModel> page = curriDAO.findAll(specification,pageable);
        return page;
    }*/
}
