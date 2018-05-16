package com.supplier.controller;

import com.jfinal.core.Controller;
import com.jfinal.plugin.ehcache.CacheKit;
import com.supplier.Msg;
import com.supplier.common.model.User;
import com.supplier.service.UserService;
import com.supplier.tools.CacheTools;

public class LoginController extends Controller {

    private UserService userService = UserService.me;

    public void login() {
        String callback=getRequest().getParameter("callback");
        try {
            String userName = getPara("userName");
            String pwd = getPara("pwd");
            User user = userService.getByUserNameAndPwd(userName, pwd);
            if (user != null) {
               setAttr("code",200);
               setAttr("msg","登陆成功");
               renderJson();
                // renderJson(user);
               // renderJson(callback+"("+Msg.SUCCESS_TXT("登陆成功")+")");
                String sid = getSession().getId();
                CacheKit.put(CacheTools.LOGIN_USER, sid, user); // 将用户信息保存到缓存，用作超时判断
            } else {
                setAttr("code",300);
                setAttr("msg","用户名密码错误");
                renderJson();
                //renderJson(callback+"("+Msg.ERROR_300("用户名密码错误")+")");
            }
        } catch (Exception e) {
            setAttr("code",300);
            setAttr("msg",e.getMessage());
            renderJson();
            //renderJson(callback+"("+Msg.ERROR_300(e.getMessage())+")");
        }
    }




}
