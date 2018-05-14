package com.supplier.service;

import com.jfinal.plugin.activerecord.Page;
import com.supplier.common.model.OrderBill;

import java.util.ArrayList;
import java.util.List;

public class OrderBillService {

    public final static OrderBillService me = new OrderBillService();

    private static OrderBill orderBillDao = new OrderBill().dao();

    public List<String> findBillNosBySupplier(String supplierId){
        List<OrderBill> list= orderBillDao.find("select * from orderBill where supplierId = ?",supplierId);
        if(list==null||list.isEmpty()){
           return new ArrayList<String>();
        }
        List<String> billNos = new ArrayList<String>();
        for(OrderBill ob:list){
          billNos.add(ob.getBillNo());
        }
        return billNos;
    }

    public Page<OrderBill> queryOrderBills(String supplierId,int pageNum, int pageSize){
        String totalRowSql = "select count(*) from orderBill where supplierId=?";
        String findSql = "select * from orderBill where supplierId = ? order by orderDate";
        return orderBillDao.paginateByFullSql(pageNum,pageSize,totalRowSql,findSql,supplierId);
    }

    public Page<OrderBill> queryOrderBillsByStatus(String supplierId,int pageNum, int pageSize,int status){
        String strStatus ="";
        switch (status){
            case 0://未执行
                strStatus = " AND status ='WZX'";
                break;
            case 1://执行中
                strStatus = " AND status !='WZX' AND status !='CH'";
                break;
            default://执行完成
                strStatus = " AND status ='CH'";
                  break;
        }
        String totalRowSql = "select count(*) from orderBill where supplierId=?"+strStatus;
        String findSql = "select * from orderBill where supplierId = ? "+strStatus+" order by orderDate";
        return orderBillDao.paginateByFullSql(pageNum,pageSize,totalRowSql,findSql,supplierId);
    }

    public OrderBill getById(Integer id){
        return orderBillDao.findById(id);
    }




}
