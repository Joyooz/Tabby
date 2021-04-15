package com.joyooz.tabby.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Problem {
    //由6位随机的数字和字母组成的字符串，区分题目的唯一标志
    private String problemId;
    //记录题目类型
    private int type;
    //标记题目目前所处的状态，如待审核、已通过、未通过等
    private int status;
    //悬赏题和活动题的积分
    private int codes;
    //题目发布的时间戳
    private String startTime;
    //题目结束的时间戳
    private String endTime;
    //题目是否原创，0表示非原创，1表示原创
    private int isOriginal;
    //记录上传题面者的userId
    private String uploader;
    //记录解答题目者的userId
    private String solver;
    //记录审核题目的管理员userId
    private String superviser;
    //记录题目所属学科
    private int subject;
    //记录题目是否已经被解答
    private int isSolved;
    //题面的图片
    private byte[] problem;
    //最佳答案的图片
    private byte[] solution;
}