package com.niguang.daxianfeng.service;

import com.niguang.daxianfeng.model.Demo;
import com.niguang.daxianfeng.mapper.DemoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DemoService {
    @Autowired
    private DemoMapper demoMapper;

    public Demo findBunkById(String demoId) {
        Demo demo = demoMapper.findDemoById(demoId);
        //model.getDemoId;
        return demo;
    }

    public void addDemo(Demo demo) {
        demoMapper.addDemo(demo);
    }

    public List<Demo> findAll() {
        List<Demo> demos = demoMapper.findAll();
        return demos;
    }
}
