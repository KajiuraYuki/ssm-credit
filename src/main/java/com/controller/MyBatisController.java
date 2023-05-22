package com.controller;

import com.mapper.CollegeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
public class MyBatisController {

    @Autowired
    private CollegeMapper collegeMapper;


    @CrossOrigin
    @ResponseBody
    @GetMapping(value = "/api/admin/getnumsPro")
    public List<Map<String, Object>> getAllNums() {
        List<Map<String, Object>> list = collegeMapper.getAllNums();
        return list;
    }
}
