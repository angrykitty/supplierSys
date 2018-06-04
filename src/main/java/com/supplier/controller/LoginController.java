package com.supplier.controller;

import com.alibaba.druid.util.StringUtils;
import com.jfinal.core.Controller;
import com.jfinal.plugin.ehcache.CacheKit;
import com.supplier.common.model.User;
import com.supplier.common.utils.Entryption;
import com.supplier.service.UserService;
import com.supplier.tools.CacheTools;

public class LoginController extends Controller {

    private UserService userService = UserService.me;

    public void login() {
        try {
            String userName = getPara("userName");
            String pwd = Entryption.encode(getPara("pwd"));
            User user = userService.getByUserNameAndPwd(userName, pwd);
            if (user != null) {
                setAttr("code", 200);
                setAttr("msg", user.setPwd(""));
                renderJson();
                // renderJson(user);
                // renderJson(callback+"("+Msg.SUCCESS_TXT("登陆成功")+")");
                String sid = getSession().getId();
                CacheKit.put(CacheTools.LOGIN_USER, sid, user); // 将用户信息保存到缓存，用作超时判断
            } else {
                setAttr("code", 300);
                setAttr("msg", "用户名密码错误");
                renderJson();
                //renderJson(callback+"("+Msg.ERROR_300("用户名密码错误")+")");
            }
        } catch (Exception e) {
            setAttr("code", 300);
            setAttr("msg", e.getMessage());
            renderJson();
            //renderJson(callback+"("+Msg.ERROR_300(e.getMessage())+")");
        }
    }

    public void logout() {
        String sid = getSession().getId();
        User user = CacheTools.getLoginUser(sid);

        if (user != null) {
            CacheTools.removeLoginUser(sid);
        }
        setAttr("code", 200);
        setAttr("msg", "登出成功");
        renderJson();

    }

    /**
     * 获取用户信息
     */
    public void getLoginUser() {
        String sid = getSession().getId();
        User user = CacheTools.getLoginUser(sid);

        if (user == null) {
            setAttr("code", 303);
            setAttr("msg", "未登陆");
            renderJson();
        } else {
            setAttr("code", 200);
            setAttr("msg", user);
            renderJson();
        }
    }

    /**
     * 新用户注册
     */
    public void addUser() {
        try {
            String userName = getPara("userName");
            String name = getPara("name");
            String pwd = Entryption.encode(getPara("pwd"));
            String supplierId = getPara("supplierId");
            String tel = getPara("tel");
            if (StringUtils.isEmpty(userName) || StringUtils.isEmpty(name) || StringUtils.isEmpty(pwd)) {
                setAttr("msg", "请填写必填信息");
                setAttr("code", 300);
                renderJson();
                return;
            }
            if (userService.findUserByUserName(userName) != null) {
                setAttr("msg", "此用户已被注册");
                setAttr("code", 300);
                renderJson();
                return;
            }
            User user = new User();
            user.setUserName(userName);
            user.setName(name);
            user.setTel(tel);
            user.setPwd(pwd);
            user.setSupplierId(supplierId);
            boolean flag = userService.addUserName(user);
            if (flag) {
                setAttr("msg", "注册成功");
                setAttr("code", 200);
                renderJson();
            } else {
                setAttr("msg", "注册失败");
                setAttr("code", 300);
                renderJson();
            }
        } catch (Exception e) {
            setAttr("msg", "用户注册失败");
            setAttr("code", 300);
            renderJson();
        }
    }

    /**
     * 修改密码
     */
    public void modifyPwd() {
        try {
            String userName = getPara("userName");
            String pwd = Entryption.encode(getPara("pwd"));
            String affirmPwd = Entryption.encode(getPara("affirmPwd"));
            if (StringUtils.isEmpty(userName) || StringUtils.isEmpty(pwd) || StringUtils.isEmpty(affirmPwd)) {
                setAttr("msg", "请填写必填信息");
                setAttr("code", 300);
                renderJson();
                return;
            }
            if (!affirmPwd.equals(pwd)) {
                setAttr("msg", "密码输入不一致");
                setAttr("code", 300);
                renderJson();
                return;
            }
            if (userService.getByUserNameAndPwd(userName, pwd) == null) {
                setAttr("msg", "用户名或密码输入错误");
                setAttr("code", 300);
                renderJson();
                return;
            }
            User user = new User();
            user.setUserName(userName);
            user.setPwd(pwd);
            boolean flag = userService.addUserName(user);
            if (flag) {
                setAttr("msg", "密码修改成功");
                setAttr("code", 200);
                renderJson();
            } else {
                setAttr("msg", "密码修改失败");
                setAttr("code", 300);
                renderJson();
            }
        } catch (Exception e) {
            setAttr("msg", "密码修改失败");
            setAttr("code", 300);
            renderJson();
        }
    }
}
