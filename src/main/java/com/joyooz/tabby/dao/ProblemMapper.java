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
    //获取未审核的题目数
    int getUnreviewedNum();
    //获取未审核的题目列表
    List<Problem> getUnreviewedList();
    //更新审核通过的题目信息
    void updateAcceptedProblem(String problemId,int codes,long startTime,long endTime,String superviser);
    //更新审核不通过的题目信息
    void updateUnacceptedProblem(String problemId,String superviser,String comment);
}
