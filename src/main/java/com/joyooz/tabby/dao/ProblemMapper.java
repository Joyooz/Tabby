package com.joyooz.tabby.dao;

import com.joyooz.tabby.entity.Problem;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ProblemMapper {
    //获取题目列表
    List<Problem> getProblemList();
    //插入新题目
    void insertProblem(String problemId,int protype,int isOriginal,String uploader,String solver,int isSolved,String problem,String solution);
}
