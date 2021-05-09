package com.joyooz.tabby.service;

import com.joyooz.tabby.dao.ProblemMapper;
import com.joyooz.tabby.entity.Problem;
import com.joyooz.tabby.entity.UnreviewedProblem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

@Configuration
public class ProblemService {
    @Autowired
    ProblemMapper problemMapper;
    @Autowired
    UserService userService;

    //根据题目id获取题目
    public Problem getProblemById(String problemId) {
        List<Problem> problemList=problemMapper.getProblemList();
        for(Problem problem:problemList) {
            if(problem.getProblemId().equals(problemId)) {
                return problem;
            }
        }
        return null;
    }

    //获取不重复的题目id
    private String newProblemId() {
        String s;
        Random random=new Random();
        boolean repeated;
        do{
            repeated=false;
            s="";
            for(int i=0;i<6;i++) {
                int num= random.nextInt(36);
                if(0<=num&&num<=9) {
                    //数字
                    s+=(char)('0'+num);
                }else{
                    //字母
                    s+=(char)('A'+num-10);
                }
            }
            List<Problem> problemList=problemMapper.getProblemList();
            for(Problem problem:problemList) {
                if(s.equals(problem.getProblemId())) repeated=true;
            }
        }while(repeated);
        return s;
    }

    //获取新的图片文件名
    private String newImgName() {
        Date date=new Date();
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyyMMddhhmmss");
        Random random=new Random();
        String s=simpleDateFormat.format(date);
        for(int i=0;i<6;i++) {
            s+= random.nextInt(10);
        }
        s+=".jpg";
        return s;
    }

    //根据图片文件名得到保存目录名字
    private String getWholePath(String imgName) {
        String str=System.getProperty("user.dir")+"/img/"+imgName;
        return str;
    }

    //根据图片字节流写入文件，如果正常返回文件名，不正常则返回“fail”
    private String writeImg(byte[] img) {
        //获取文件夹地址 并检查文件夹
        String dict=System.getProperty("user.dir")+"/img";
        File dictFile=new File(dict);
        if(!dictFile.exists()&&!dictFile.isDirectory()) {
            dictFile.mkdir();
        }
        //获取文件名和地址
        String filename=newImgName();
        String path=getWholePath(filename);
        File imgFile=new File(path);
        try{
            FileOutputStream fileOutputStream=new FileOutputStream(imgFile);
            fileOutputStream.write(img);
            fileOutputStream.close();
            return filename;
        }catch(Exception e) {
            e.printStackTrace();
            return "fail";
        }

    }

    //添加题目至数据库，返回状态
    public String createProblem(byte[] problemImg,byte[] solutionImg,int protype,String userId,int isOriginal) {
        String problemId=newProblemId();
        //普通题和活动题
        if(protype==1||protype==2) {
            if(problemImg==null||solutionImg==null) {
                return "NO";
            }
            String res1=writeImg(problemImg);//保存题目图片
            String res2=writeImg(solutionImg);//保存解答图片
            //写入文件出现问题
            if(res1.equals("fail")||res2.equals("fail")) {
                return "NO";
            }
            try{
                problemMapper.insertProblem(problemId,protype,isOriginal,userId,userId,1,res1,res2);
                return "YES";
            }catch (Exception e) {
                return "NO";
            }
        }
        //悬赏题
        else{
            if(problemImg==null) {
                return "NO";
            }
            String res=writeImg(problemImg);//保存题目图片
            //写入文件出现问题
            if(res.equals("fail")) {
                return "NO";
            }
            try{
                problemMapper.insertProblem(problemId,protype,isOriginal,userId,"",0,res,"");
                Date date=new Date();
                userService.updateLastUploadTime(userId,date.getTime());
                return "YES";
            }catch (Exception e) {
                return "NO";
            }
        }
    }

    //管理员获取未审核的题目数
    public int getUnreviewedNum() {
        try{
            return problemMapper.getUnreviewedNum();
        }catch(Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    //管理员获取第一个未审核的题目
    public UnreviewedProblem getUnreviewedPro() {
        //校验是否有题目
        if(getUnreviewedNum()==0) {
            return new UnreviewedProblem("null",0,0,"","");
        }else{
            try{
                List<Problem> problemList= problemMapper.getUnreviewedList();
                Problem pro=problemList.get(0);
                return new UnreviewedProblem(pro.getProblemId(),pro.getProtype(),pro.getIsOriginal(),pro.getProblem(),pro.getSolution());
            }catch(Exception e) {
                return new UnreviewedProblem("null",0,0,"","");
            }
        }

    }

    //管理员提交审核结果
    public String commitReview(String superviser,String problemId,int accepted,int codes,int lastTime,String comment) {
        //判断题目是否存在且状态为待审核
        Problem pro=getProblemById(problemId);
        if(pro==null) {
            return "NO";
        }
        if(pro.getStatus()!=0) {
            return "NO";
        }
        //审核通过
        if(accepted==1) {
            Date date=new Date();
            long startTime=date.getTime();
            int lastMS=lastTime*24*3600*1000;
            long endTime=startTime+lastMS;
            try{
                problemMapper.updateAcceptedProblem(problemId,codes,startTime,endTime,superviser);
                return "YES";
            }catch (Exception e) {
                e.printStackTrace();
                return "NO";
            }
        }
        //审核不通过
        if(accepted!=1) {
            try{
                problemMapper.updateUnacceptedProblem(problemId,superviser,comment);
                return "YES";
            }catch (Exception e) {
                e.printStackTrace();
                return "NO";
            }
        }
        return "NO";
    }


}
