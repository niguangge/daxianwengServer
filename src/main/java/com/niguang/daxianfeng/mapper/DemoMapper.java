package com.niguang.daxianfeng.mapper;

import com.niguang.daxianfeng.model.Demo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface DemoMapper {
    @Select("SELECT * FROM model WHERE demo_id = #{demoId};")
    public Demo findDemoById(@Param("demoId") String demoId);

    @Insert("INSERT INTO model(demo_id,demo_name) VALUES(#{demoId},#{demoName});")
    public void addDemo(Demo demo);

    @Select("SELECT * FROM model;")
    public List<Demo> findAll();

}
