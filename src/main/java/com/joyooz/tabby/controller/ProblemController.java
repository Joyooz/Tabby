package com.joyooz.tabby.controller;

import com.joyooz.tabby.entity.UnreviewedProblem;
import com.joyooz.tabby.entity.User;
import com.joyooz.tabby.service.ProblemService;
import com.joyooz.tabby.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.util.Date;

@RestController
public class ProblemController {
    @Autowired
    ProblemService problemService;

    @Autowired
    UserService userService;

    @PostMapping("/uploadProblem")
    public String uploadProblem(@RequestParam("problem")MultipartFile problem, @RequestParam("solution")MultipartFile solution, @RequestParam("protype")int protype, @RequestParam("isOriginal")int isOriginal, HttpSession session) {
        //判断用户是否登录
        if(session.getAttribute("user")==null) {
            return "NO";
        }
        //获取用户id
        User user=(User)session.getAttribute("user");
        String userId=user.getUserId();
        //对比用户上次上传时间戳，是否大于3600
        Date date=new Date();
        user=userService.getUserByUserId(userId);
        if(date.getTime()-user.getLastUploadTime()<=3600) return "NO";

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

    @GetMapping("/getUnreviewedNum")
    public int getUnreviewedNum(HttpSession session) {
        //身份校验
        if(session.getAttribute("user")==null) {
            return -1;
        }else{
            try{
                User user=(User)session.getAttribute("user");
                if(user.getUserType()!=1) {
                    return -1;
                }
            }catch (Exception e) {
                e.printStackTrace();
                return -1;
            }
        }
        //身份校验成功
        return problemService.getUnreviewedNum();
    }

    @GetMapping("/getUnreviewedPro")
    public UnreviewedProblem getUnreviewedPro(HttpSession session) {
        //身份校验
        if(session.getAttribute("user")==null) {
            return null;
        }else{
            try{
                User user=(User)session.getAttribute("user");
                if(user.getUserType()!=1) {
                    return null;
                }
            }catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        //身份校验成功
        return problemService.getUnreviewedPro();
    }

    @PostMapping("/commitReview")
    public String commitReview(@RequestParam("problemId")String problemId,@RequestParam("accepted")int accepted,@RequestParam("codes")int codes,@RequestParam("lastTime")int lastTime,@RequestParam("comment")String comment,HttpSession session) {
        //身份校验
        User user;
        if(session.getAttribute("user")==null) {
            return "NO";
        }else{
            try{
                user=(User)session.getAttribute("user");
                if(user.getUserType()!=1) {
                    return "NO";
                }
            }catch (Exception e) {
                e.printStackTrace();
                return "NO";
            }
        }
        //身份校验成功,数据校验
        if(problemId.equals("")) {
            return "NO";
        }
        if(accepted!=0&&accepted!=1) {
            return "NO";
        }
        if(codes<0) {
            return "NO";
        }
        if(lastTime<0) {
            return "NO";
        }
        //数据校验成功，正常返回
        return problemService.commitReview(user.getUserId(),problemId,accepted,codes,lastTime,comment);
    }


}
