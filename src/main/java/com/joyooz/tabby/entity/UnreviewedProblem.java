package com.joyooz.tabby.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UnreviewedProblem {
    private String problemId;
    private int protype;
    private int isOriginal;
    private String problem;
    private String solution;
    public UnreviewedProblem(String problemId,int protype,int isOriginal,String problem,String solution) {
        this.problemId=problemId;
        this.protype=protype;
        this.isOriginal=isOriginal;
        this.problem=problem;
        this.solution=solution;
    }
}
