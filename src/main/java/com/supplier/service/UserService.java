package com.supplier.service;

import com.supplier.common.model.User;

public class UserService {
    public static final UserService me = new UserService();

    private User userDao = new User().dao();

    //用户名密码查找用户
    public User getByUserNameAndPwd(String userName,String pwd)throws Exception{

        User user = userDao.findFirst("SELECT * FROM user WHERE userName = ? AND pwd = ?",userName,pwd);

        return user;
    }





}
