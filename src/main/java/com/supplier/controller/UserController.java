package com.supplier.controller;

import com.alibaba.druid.util.StringUtils;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Page;
import com.supplier.common.model.User;
import com.supplier.common.utils.Entryption;
import com.supplier.service.UserService;

public class UserController extends Controller {

    private final static UserService userService = UserService.me;

    private static final String DEFAULT_PWD = "123456";

    /**
     * 获取用户提交的列表
     */
    public void findPageUser() {
        Page<User> page = null;
        try {
            Integer pageNum = getParaToInt("pageNum");
            Integer pageSize = getParaToInt("pageSize");
            String name = getPara("name");
            String supplierId = getPara("supplierId");
            String startTime = getPara("startTime");
            String endTime = getPara("endTime");
            page = userService.findPageUser(pageNum, pageSize, name, supplierId, startTime, endTime);
            setAttr("message", page);
            setAttr("code", "200");
            renderJson();
            return;
        } catch (Exception e) {
            setAttr("message", page);
            setAttr("code", "300");
            renderJson();
            return;
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
            User user = userService.getByUserNameAndPwd(userName, pwd);
            if (user == null) {
                setAttr("msg", "用户名或密码输入错误");
                setAttr("code", 300);
                renderJson();
                return;
            }
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

    /**
     * 重置密码
     */
    public void resettingPwd() {
        String userName = getPara("userName");
        String pwd = Entryption.encode(getPara("pwd"));
        try {
            if (StringUtils.isEmpty(userName) || StringUtils.isEmpty(pwd)) {
                setAttr("msg", "请填写必填信息");
                setAttr("code", 300);
                renderJson();
                return;
            }
            User user = userService.getByUserNameAndPwd(userName, pwd);
            if (user == null) {
                setAttr("msg", "原密码输入错误");
                setAttr("code", 300);
                renderJson();
                return;
            }
            user.setUserName(userName);
            user.setPwd(Entryption.encode(DEFAULT_PWD));
            boolean flag = userService.modifyUser(user);
            if (flag) {
                setAttr("msg", "密码重置成功");
                setAttr("code", 200);
                renderJson();
                return;
            } else {
                setAttr("msg", "密码重置失败");
                setAttr("code", 300);
                renderJson();
                return;
            }
        } catch (Exception e) {
            setAttr("msg", "密码重置失败");
            setAttr("code", 300);
            renderJson();
        }
    }
}
