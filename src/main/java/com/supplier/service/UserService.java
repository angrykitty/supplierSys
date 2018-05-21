package com.supplier.service;

import com.alibaba.druid.util.StringUtils;
import com.supplier.common.model.User;

public class UserService {
    public static final UserService me = new UserService();

    private User userDao = new User().dao();

    //用户名密码查找用户
    public User getByUserNameAndPwd(String userName,String pwd)throws Exception{

        User user = userDao.findFirst("SELECT * FROM user WHERE userName = ? AND pwd = ?",userName,pwd);

        return user;
    }
    public User createUser(User user)throws Exception{
        if (user ==null){
            throw new Exception("user is required");
        }
        if(StringUtils.isEmpty(user.getName())){
            throw new Exception("user.name is required");
        }
        if(StringUtils.isEmpty(user.getPwd())){
            throw new Exception("user.pwd is required");
        }
        if(StringUtils.isEmpty(user.getUserName())){
            throw new Exception("user.userName is required");
        }
        if(StringUtils.isEmpty(user.getSupplierId())){
            throw new Exception("user.supplierId is required");
        }
        if(StringUtils.isEmpty(user.getName())){
            throw new Exception("user is required");
        }
        if(StringUtils.isEmpty(user.getName())){
            throw new Exception("user is required");
        }
        return null;
    }



}