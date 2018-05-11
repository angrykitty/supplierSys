package com.supplier.tools;

import com.jfinal.plugin.ehcache.CacheKit;
import com.supplier.common.model.User;

public class CacheTools {
    public static final String  LOGIN_USER ="LoginUserCache";

    public static User getLoginUser(String sid){
        return CacheKit.get(LOGIN_USER,sid);
    }
}
