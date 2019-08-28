package com.example.controller;

import com.example.constant.ResultFactory;
import com.example.constant.RestResult;
import com.example.entity.User;
import com.example.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Controller
@RequestMapping("/user")
public class LoginController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private UserService userService;     //跳转首页（登录页）

    @RequestMapping("/toIndex")
    public String show(){
        return "index";
    }

    //登录操作
    @ResponseBody
    @RequestMapping("/loginUser")
    public RestResult login(User user, HttpServletRequest request){

        User u1 =userService.login(user.getUsername(),user.getPassword());
        if(u1 != null) {
            //储存到session中
            request.getSession().setAttribute("user",user);
            return ResultFactory.getSuccessResult("登陆成功",user);
        }
        return ResultFactory.getFailResult("用户名/密码错误");

    }

    //跳转注册页
//    @RequestMapping("/toRegister")
//    public String toRegister(){
//        return "register";
//    }

    //注册操作
    @RequestMapping("/register")
    public String register(User user){
        int su = userService.register(user);
        if(su==0){
            System.out.println("----");
        }        return "welcome";
    }

    //测试未登陆拦截页面
    @RequestMapping("/welcome")
    public String welcome(){
        return "welcome";
    }

    //退出登录
    @RequestMapping("/outUser")
    public void outUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.getSession().removeAttribute("session_user");
        response.sendRedirect("/user/toIndex");
    }
}
