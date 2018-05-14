package com.supplier.controller;

import com.jfinal.core.Controller;
import com.jfinal.kit.JsonKit;
import com.jfinal.plugin.activerecord.Page;
import com.supplier.Msg;
import com.supplier.common.model.OrderBill;
import com.supplier.common.model.OrderBillProcedures;
import com.supplier.common.model.User;
import com.supplier.service.OrderBillProceduresService;
import com.supplier.service.OrderBillService;
import com.supplier.tools.CacheTools;

import java.util.List;

public class OrderBillController extends Controller{

    private OrderBillService obService = OrderBillService.me;

    private OrderBillProceduresService obpService = OrderBillProceduresService.me;

    public void queryOrderBillsByStatus(){
        String callback=getRequest().getParameter("callback");
        try {
            User user = CacheTools.getLoginUser(getSession().getId());
            Integer billStatus = getParaToInt("billStatus");
            Integer pageNum = getParaToInt("pageNum");
            Integer pageSize = getParaToInt("pageSize");
            Page<OrderBill> page = obService.queryOrderBillsByStatus(user.getSupplierId(),pageNum,pageSize,billStatus);
            page = page==null?new Page<OrderBill>():page;
            renderJson(callback+"("+ Msg.SUCCESS_OBJ(page)+")");
        }catch (Exception e) {
            renderJson(callback + "(" + Msg.ERROR_300("参数错误") + ")");
        }
    }

    public void findProcedures(){
        String callback=getRequest().getParameter("callback");
        try {
            User user = CacheTools.getLoginUser(getSession().getId());
            Integer billId = getParaToInt("billId");

            OrderBill ob = obService.getById(billId);
            if(!ob.getSupplierId().trim().equals(user.getSupplierId().trim())){
                renderJson(callback + "(" + Msg.ERROR_300("无权访问该数据") + ")");
                return;
            }
            List<OrderBillProcedures> list = obpService.findByFid(billId);
            renderJson(callback+"("+ Msg.SUCCESS_OBJ(list)+")");
        }catch (Exception e) {
            renderJson(callback + "(" + Msg.ERROR_300("参数错误") + ")");
        }

    }

    public void getOrderBillById(){
        String callback=getRequest().getParameter("callback");
        Integer billId = getParaToInt("billId");
        OrderBill ob = obService.getById(billId);
        renderJson(callback+"("+ Msg.SUCCESS_OBJ(ob)+")");
    }




}
