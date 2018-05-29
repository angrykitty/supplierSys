package com.supplier.controller;

import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Page;
import com.supplier.common.model.OrderBill;
import com.supplier.common.model.OrderBillProcedures;
import com.supplier.common.model.User;
import com.supplier.service.OrderBillProceduresService;
import com.supplier.service.OrderBillService;
import com.supplier.tools.CacheTools;

import java.util.*;

public class OrderBillController extends Controller{

    private OrderBillService obService = OrderBillService.me;

    private OrderBillProceduresService obpService = OrderBillProceduresService.me;

    //查询列表
    public void queryOrderBillsByStatus(){
        try {
            User user = CacheTools.getLoginUser(getSession().getId());
            Integer billStatus = getParaToInt("billStatus");
            Integer pageNum = getParaToInt("pageNum");
            Integer pageSize = getParaToInt("pageSize");
            Page<OrderBill> page = null;
            if(user.getRole().intValue()==1) {
               page  = obService.queryOrderBillsByStatus(user.getSupplierId(), pageNum, pageSize, billStatus);
            }else {
                page = obService.queryOrderBillsByStatusForBuyer(user.getSupplierId(),pageNum,pageSize,billStatus);
            }

            page = page==null?new Page<OrderBill>():page;
            setAttr("msg",page);
            setAttr("code",200);
            renderJson();
           //renderJson(callback+"("+ Msg.SUCCESS_OBJ(page)+")");
        }catch (Exception e) {
            setAttr("msg","参数错误");
            setAttr("code",300);
            renderJson();
            //renderJson(callback + "(" + Msg.ERROR_300("参数错误") + ")");
        }
    }

  //查询列表（PC端）
    public void queryOrderBillsByStatusForWeb(){
        try {
            User user = CacheTools.getLoginUser(getSession().getId());
            Integer billStatus = getParaToInt("billStatus");
            Integer pageNum = getParaToInt("pageNum");
            Integer pageSize = getParaToInt("pageSize");
            Page<OrderBill> page = null;
            if(user.getRole().intValue()==1) {
                page  = obService.queryOrderBillsByStatus(user.getSupplierId(), pageNum, pageSize, billStatus);
            }else {
                page = obService.queryOrderBillsByStatusForBuyer(user.getSupplierId(),pageNum,pageSize,billStatus);
            }
            page = page==null?new Page<OrderBill>():page;
            List<OrderBill> obs = page.getList();
            List<Map<String,Object>> maps = new ArrayList<Map<String, Object>>();
            for(OrderBill ob:obs){
                if(ob==null) continue;
                Map<String,Object> map = new HashMap<String, Object>();
                map.put("orderBill",ob);
                map.put("procedures",obpService.findByFid(ob.getId()));
                maps.add(map);
            }
            Page<Map<String,Object>> mPage = new Page<Map<String, Object>>(maps,page.getPageNumber(),pageSize,page.getTotalPage(),page.getTotalRow());
            setAttr("msg",mPage);
            setAttr("code",200);
            renderJson();
            //   renderJson(callback+"("+ Msg.SUCCESS_OBJ(page)+")");
        }catch (Exception e) {
            setAttr("msg","参数错误");
            setAttr("code",300);
            renderJson();
            //renderJson(callback + "(" + Msg.ERROR_300("参数错误") + ")");
        }
    }


    public void findProcedures(){
       // String callback=getRequest().getParameter("callback");
        try {
            User user = CacheTools.getLoginUser(getSession().getId());
            Integer billId = getParaToInt("billId");

            OrderBill ob = obService.getById(billId);
            if(!ob.getSupplierId().trim().equals(user.getSupplierId().trim())){
                setAttr("code",300);
                setAttr("msg","无权访问该数据");
                renderJson();
                return;
            }
            List<OrderBillProcedures> list = obpService.findByFid(billId);
            setAttr("msg",list);
            setAttr("code",200);
            renderJson();
         //   renderJson(callback+"("+ Msg.SUCCESS_OBJ(list)+")");
        }catch (Exception e) {
            setAttr("msg","参数错误");
            setAttr("code",300);
            renderJson();
            //renderJson(callback + "(" + Msg.ERROR_300("参数错误") + ")");
        }

    }

    public void getOrderBillById(){
        //String callback=getRequest().getParameter("callback");
        Integer billId = getParaToInt("billId");
        OrderBill ob = obService.getById(billId);
        setAttr("msg",ob);
        setAttr("code",200);
        renderJson();
        //  renderJson(callback+"("+ Msg.SUCCESS_OBJ(ob)+")");
    }

    /*public void queryOrderBillsByStatusForBuyer(){
        try {
            User user = CacheTools.getLoginUser(getSession().getId());
            Integer billStatus = getParaToInt("billStatus");
            Integer pageNum = getParaToInt("pageNum");
            Integer pageSize = getParaToInt("pageSize");
            Page<OrderBill> page = obService.queryOrderBillsByStatusForBuyer(user.getSupplierId(),pageNum,pageSize,billStatus);
            page = page==null?new Page<OrderBill>():page;
            setAttr("msg",page);
            setAttr("code",200);
            renderJson();
            //renderJson(callback+"("+ Msg.SUCCESS_OBJ(page)+")");
        }catch (Exception e) {
            setAttr("msg","参数错误");
            setAttr("code",300);
            renderJson();
            //renderJson(callback + "(" + Msg.ERROR_300("参数错误") + ")");
        }
    }*/

    public void updateDateForProcedures(){
       // User user = CacheTools.getLoginUser(getSession().getId());
        Integer pId = getParaToInt("pId");
        Date date = getParaToDate("dt");
        if(pId==null){
            setAttr("msg","参数错误");
            setAttr("code",300);
            renderJson();
            return;
        }
        OrderBillProcedures obp =  obpService.getById(pId);
        if(obp==null){
            setAttr("msg","ID不存在");
            setAttr("code",300);
            renderJson();
            return;
        }
        obp.setDeadline(date);
        obp.update();
        setAttr("msg","更新成功");
        setAttr("code",200);
        renderJson();
    }




}
