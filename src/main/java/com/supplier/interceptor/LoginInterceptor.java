package com.supplier.interceptor;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.core.Controller;
import com.supplier.Msg;
import com.supplier.common.model.User;
import com.supplier.tools.CacheTools;

public class LoginInterceptor implements Interceptor {


    public void intercept(Invocation invocation) {
        Controller ctl=invocation.getController();
        //判断用户是否登录
        String sid=ctl.getSession().getId();
        User user= CacheTools.getLoginUser(sid);
        String action=invocation.getActionKey();
        if(user!=null || action.indexOf("login")>=0){
            invocation.invoke();
        }else{
            ctl.setAttr("code",303);
            ctl.setAttr("msg","身份验证失败");
            ctl.renderJson();
        }
    }

}