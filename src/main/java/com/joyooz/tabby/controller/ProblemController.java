package com.joyooz.tabby.controller;

import com.joyooz.tabby.entity.User;
import com.joyooz.tabby.service.ProblemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;

@RestController
public class ProblemController {
    @Autowired
    ProblemService problemService;

    @PostMapping("/uploadProblem")
    public String uploadProblem(@RequestParam("problem")MultipartFile problem, @RequestParam("solution")MultipartFile solution, @RequestParam("protype")int protype, @RequestParam("isOriginal")int isOriginal, HttpSession session) {
        //判断用户是否登录
        if(session.getAttribute("user")==null) {
            return "NO";
        }
        //获取用户id
        User user=(User)session.getAttribute("user");
        String userId=user.getUserId();

        byte[] pro=null,sol=null;
        //校验参数,暂未进行jpg验证
        if(protype!=1&&protype!=2&&protype!=3) {
            return "NO";
        }
        if(isOriginal!=0&&isOriginal!=1) {
            return "NO";
        }
        try{
            if(problem==null) {
                return "NO";
            }else{
                pro=problem.getBytes();
            }
            if(solution!=null) {
                sol=solution.getBytes();
            }
            return problemService.createProblem(pro,sol,protype,userId,isOriginal);
        }catch (Exception e) {
            e.printStackTrace();
            return "NO";
        }
    }
}
