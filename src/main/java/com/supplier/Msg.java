package com.supplier;

import com.jfinal.json.Json;
import com.jfinal.kit.JsonKit;

public class Msg {
    private Integer code;
    private String msg;

    public final static String  ERROR_500(String msg){
        return Json.getJson().toJson(new Msg(500,msg));
    }

    public final static String  ERROR_300(String msg){
        return Json.getJson().toJson(new Msg(300,msg));
    }

    public final static String  SUCCESS_TXT(String msg){
        return Json.getJson().toJson(new Msg(200,msg));
    }

    public final static String  SUCCESS_OBJ(Object obj){
        return "{\"code\":200,\"msg\":"+JsonKit.toJson(obj)+"}";
    }


    public Msg(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
